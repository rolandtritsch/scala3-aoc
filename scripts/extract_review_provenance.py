#!/usr/bin/env python3
"""Extract solver-run provenance evidence from the Paperclip API for a review.

The aoc-review contract records solverRuns with runId, logBytes and toolCalls
for every cleared computational review. This helper pulls those values from the
authenticated API (instead of parsing redacted logs by hand) and prints a
structured report plus a ready-to-paste fenced JSON fragment for the result
block's provenance.solverRuns entry.

It also prints the tool-call breakdown, any external URLs or web tools, and a
decoded-log keyword scan so the reviewer can complete the actual contamination
audit without hand-parsing the double-escaped NDJSON. This script only reads; it
never submits, publishes or creates tasks. The reviewer still inspects the log.

Inputs
------
  --issue-id ISSUE_ID    List this issue's runs (solver, gatherer, submitter,
                         reviewer) so you can pick the solver run id; exits
                         after listing when --run-id is omitted.
  --run-id RUN_ID        A solver heartbeat run id (e.g. 352ede35-...).
  --log-file FILE        Optional: scan a saved redacted log excerpt file
                         (JSON as returned by /api/heartbeat-runs/{id}/log)
                         instead of fetching it. Skips run metadata and the
                         fragment; used for offline validation of the scan.
  --commit FULL_SHA      Reviewed solution commit. Confirms the audited run's
                         decoded log references that exact full (and short)
                         hash, binding the run to the reviewed commit. Fails
                         the audit (exit 1) if absent, so a run that did not
                         produce/publish the reviewed commit cannot clear
                         provenance silently.
  --no-keyword-scan      Disable the decoded-log keyword contamination scan.
  --day NN               Scope the keyword scan to the reviewed day: adds
                         day-specific solution-source patterns (e.g. "day 4
                         solution", a day04 GitHub repository path) on top of
                         the generic scan. The generic scan alone only covers
                         cross-day sites (Reddit, StackOverflow, ...) and the
                         old hard-coded Day02 patterns.
  --find-solver --year YYYY --day NN --part P --commit FULL_SHA
                         Locate the solve issue for the given year/day/part in
                         this company, list its runs, audit each succeeded run
                         for the reviewed commit, and print the provenance
                         fragment for the run(s) that reference it. Automates
                         the manual "grep issue titles for the solve issue,
                         then try each run until --commit binds" step.

Environment (auto-injected): PAPERCLIP_API_URL, PAPERCLIP_API_KEY,
PAPERCLIP_COMPANY_ID. In bridge/queue environments the run's `AOC_PAPERCLIP_*`
variables carry the host-API credentials that actually verify against
`PAPERCLIP_API_URL`, so those are preferred when present; otherwise the script
falls back to the standard `PAPERCLIP_*` pair.

Output
------
  Plain-text report to stdout: run metadata, tool-call counts, files touched,
  bash commands, external URLs, keyword-scan hits, and a fenced
  ```aoc-solver-log fragment with the runId/logBytes/toolCalls to embed in the
  aoc-review-result block.

Validation
----------
  Run against a real solver run and confirm logBytes matches the run metadata,
  toolCalls is the parsed count, and the emitted fragment parses as JSON.
  Run --issue-id alone and confirm it lists runs and exits 0. Run --log-file
  against a saved excerpt with and without planted contamination keywords and
  confirm the scan reports exactly those hits. Pass --commit with both a
  matching full hash (exit 0) and a bogus hash (exit 1) to confirm the binding
  check is enforced. Failure cases: missing/invalid run id, empty or unreadable
  log (exit non-zero), or --commit not present in the decoded log (exit 1).
"""

import argparse
import json
import os
import re
import sys
import urllib.request
from collections import Counter
from urllib.error import HTTPError, URLError

MAX_CALL = 1024 * 1024  # log endpoint max 1 MiB per call
ALLOWED_API_HOSTS = {"paperclip.tritsch.org", "paperclip.ing"}


def resolve_api():
    """Return (base_url, key) preferring the host-verified AOC_* pair.

    In bridge/queue run environments the injected `PAPERCLIP_API_KEY` is a
    short-lived bridge token that does not verify against the host API, while
    the `AOC_PAPERCLIP_API_URL`/`AOC_PAPERCLIP_API_KEY` pair carries the real
    host credentials. Prefer those when present so reviewers do not have to
    export them by hand; otherwise fall back to the standard pair.
    """
    pairs = (
        ("AOC_PAPERCLIP_API_URL", "AOC_PAPERCLIP_API_KEY"),
        ("PAPERCLIP_API_URL", "PAPERCLIP_API_KEY"),
    )
    for url_var, key_var in pairs:
        base = os.environ.get(url_var, "").rstrip("/")
        key = os.environ.get(key_var, "")
        if base and key:
            return base, key
    raise SystemExit("PAPERCLIP_API_URL and PAPERCLIP_API_KEY are required")


API, KEY = resolve_api()


def api_get(path):
    if not API or not KEY:
        raise SystemExit("PAPERCLIP_API_URL and PAPERCLIP_API_KEY are required")
    req = urllib.request.Request(
        API + path, headers={"Authorization": f"Bearer {KEY}"}
    )
    with urllib.request.urlopen(req) as resp:
        return json.loads(resp.read().decode())


def fetch_log(run_id):
    """Return (records, fetched_bytes) by paginating the redacted log excerpt."""
    records = []
    offset = 0
    while True:
        path = f"/api/heartbeat-runs/{run_id}/log?offset={offset}&limitBytes={MAX_CALL}"
        try:
            data = api_get(path)
        except HTTPError as err:
            raise SystemExit(f"log read failed HTTP {err.code}: {err.reason}")
        except URLError as err:
            raise SystemExit(f"log read failed: {err.reason}")
        text = json.dumps(data)
        fetched = len(text.encode())
        content = data.get("content", "") if isinstance(data, dict) else data
        for line in content.splitlines():
            line = line.strip()
            if not line:
                continue
            try:
                records.append(json.loads(line))
            except ValueError:
                continue
        if fetched < MAX_CALL:
            break
        offset += 1
    return records, fetched


def iter_tool_chunks(records):
    """Yield tool_use chunks from the double-escaped NDJSON records."""
    for rec in records:
        chunk = rec.get("chunk")
        if not chunk:
            continue
        try:
            inner = json.loads(chunk)
        except ValueError:
            continue
        if inner.get("type") == "tool_use":
            yield inner


def external_urls(text):
    urls = set()
    for u in re.findall(r"https?://[^\s\"'\\,)>]+", text):
        host = re.sub(r"^https?://", "", u).split("/")[0].split(":")[0]
        if host and host not in ALLOWED_API_HOSTS:
            urls.add(u)
    return urls


# Solution-source signals the reviewer must triage. "editorial"/"walkthrough"
# also appear in the company skills' provenance warnings, so each hit is
# printed with the tool or text chunk it came from for the reviewer to judge.
# Day-specific patterns are added per reviewed day so e.g. "day 4 solution"
# or a day04 GitHub repository is flagged, not just Day02.
KEYWORDS = (
    r"reddit\.com",
    r"stackoverflow",
    r"leetcode",
    r"codeforces",
    r"adventofcode-solutions",
    r"aoc-solutions",
    r"answer list",
    r"spreadsheet of answers",
)


def day_keywords(day):
    """Return day-specific solution-source regexes for the reviewed day.

    Catches "day NN solution", "solution for day NN", "day NN answer", and
    solution repositories whose path names the day (day04, day-04, day4, ...).
    Patterns are kept narrow to avoid self-referential false positives (the
    solver's own handoff legitimately says "Day04 ... solution commit"). The
    generic KEYWORDS above still catch cross-day sites (Reddit, SO, ...).
    """
    d1 = str(day)
    return (
        rf"github\.com/[^'\"]*/[^'\"]*day[_-]?0?{day}\b",
        rf"\bday\s+{d1}\s+solution\b",
        rf"\bsolution\s+for\s+day\s+{d1}\b",
        rf"\bday\s+{d1}\s+answer\b",
        rf"\banswer\s+for\s+day\s+{d1}\b",
    )


def keyword_hits(records, day=None, limit=200):
    """Return (keyword, source_label, snippet) hits from decoded tool inputs
    and text chunks. `source_label` is the tool name or "text". When `day` is
    given, day-specific solution patterns are added to the generic scan."""
    keywords = list(KEYWORDS)
    if day:
        keywords.extend(day_keywords(day))
    hits = []
    seen = set()
    for rec in records:
        chunk = rec.get("chunk")
        if not chunk:
            continue
        try:
            inner = json.loads(chunk)
        except ValueError:
            continue
        part = inner.get("part", {})
        if inner.get("type") == "tool_use":
            tool = part.get("tool")
            inp = (part.get("state") or {}).get("input") or {}
            text = json.dumps(inp)
        elif inner.get("type") == "text":
            tool = "text"
            text = part.get("text") or part.get("output") or ""
        else:
            continue
        low = text.lower()
        for kw in keywords:
            for m in re.finditer(kw, low):
                if len(seen) >= limit:
                    return hits
                start = max(0, m.start() - 60)
                end = min(len(text), m.end() + 60)
                snippet = text[start:end].replace("\n", " ")
                dedup = (kw, tool, snippet[:120])
                if dedup in seen:
                    continue
                seen.add(dedup)
                hits.append((kw, tool, snippet))
    return hits


def commit_binding(records, full_sha):
    """Confirm the decoded log references the reviewed full commit hash.

    Searches tool inputs and outputs as well as assistant text chunks, so a
    run that produced or published the reviewed commit (e.g. a publish receipt
    echoed to the bash output) binds even when the hash only appears after
    the tool ran. Returns (full_hits, short_only) counts for the full and the
    short 7-char form.
    """
    short = full_sha[:7]
    full_hits = 0
    short_only = 0
    for rec in records:
        chunk = rec.get("chunk")
        if not chunk:
            continue
        try:
            inner = json.loads(chunk)
        except ValueError:
            continue
        part = inner.get("part", {})
        texts = []
        if inner.get("type") == "tool_use":
            state = part.get("state") or {}
            texts.append(json.dumps(state.get("input") or {}))
            texts.append(json.dumps(state.get("output") or {}))
        else:
            texts.append(part.get("text") or part.get("output") or "")
        for text in texts:
            full_hits += len(re.findall(re.escape(full_sha), text))
            short_only += len(re.findall(
                r"(?<![0-9a-f])" + re.escape(short) + r"(?![0-9a-f])", text))
    return full_hits, short_only


def resolve_company_id():
    """Return the company id, preferring the AOC_* bridge variant when set."""
    return (os.environ.get("AOC_PAPERCLIP_COMPANY_ID")
            or os.environ.get("PAPERCLIP_COMPANY_ID", ""))


def find_solve_issue(year, day, part, none_reason=None):
    """Return the id of this company's `AoC YYYY DayNN partP: solve` issue."""
    company = resolve_company_id()
    if not company:
        raise SystemExit("PAPERCLIP_COMPANY_ID is required for --find-solver")
    title = "AoC {0} Day{1:02d} part{2}: solve".format(year, day, part)
    issues = api_get("/api/companies/{0}/issues".format(company))
    for issue in issues:
        if issue.get("title") == title:
            return issue.get("id")
    if none_reason:
        raise SystemExit(none_reason)
    raise SystemExit("no solve issue found with title: {0}".format(title))


def find_solver_run(year, day, part, commit):
    """Audit every succeeded run of the solve issue and report the one(s)
    whose decoded log references the reviewed commit. Prints the provenance
    fragment for the bound run and returns 0, or 1 when none binds.

    When this company has no current-cycle `AoC YYYY DayNN partP: solve`
    issue, the review is reconciled historical accepted work (CTO re-recorded
    an existing repo solution; the original solve predates the current cycle
    and lives in a prior company instance). Its solver derivation log is not
    retrievable here, so provenance cannot be certified "clear" from a current
    tool-log audit. Print that evidence-gap diagnostic and return 2."""
    try:
        issue_id = find_solve_issue(year, day, part)
    except SystemExit:
        print("No current-cycle solve issue for AoC {0} Day{1:02d} part{2}."
              .format(year, day, part))
        print("This is reconciled historical accepted work: the CTO re-recorded an")
        print("existing repository solution whose original solve predates the current")
        print("cycle (prior company instance). Its solver derivation log is not")
        print("retrievable from this company, so the solver-run provenance evidence")
        print("for this review is a genuine gap.")
        print("Correct disposition: verify correctness independently, then record the")
        print("aoc-review-result with provenance status \"unknown\" (or escalate the")
        print("missing evidence to the CTO). Do NOT certify \"clear\" from the CTO")
        print("reconcile run alone.")
        return 2
    print("Solve issue: {0}".format(issue_id))
    runs = api_get("/api/issues/{0}/runs".format(issue_id))
    bound = []
    for r in runs:
        if r.get("status") != "succeeded":
            continue
        run_id = r.get("id") or r.get("runId")
        print("\n--- auditing run {0} (agent {1}) ---".format(
            run_id, r.get("agentId") or r.get("agent") or "?"))
        meta = api_get("/api/heartbeat-runs/{0}".format(run_id))
        log_bytes = meta.get("logBytes", 0)
        print("  logBytes: {0}".format(log_bytes))
        records, _ = fetch_log(run_id)
        full_hits, short_only = commit_binding(records, commit)
        print("  commit {0}: full-hash hits={1}, short-hash hits={2}".format(
            commit[:7], full_hits, short_only))
        if full_hits > 0:
            tools = Counter()
            for chunk in iter_tool_chunks(records):
                tools[chunk.get("part", {}).get("tool")] += 1
            tool_calls = sum(tools.values())
            bound.append({"runId": run_id, "logBytes": log_bytes,
                          "toolCalls": tool_calls, "complete": True})
    if not bound:
        print("\nNo succeeded run references commit {0}.".format(commit[:7]))
        return 1
    print("\nFragments for provenance.solverRuns[] (audit each for contamination):")
    for frag in bound:
        print("```aoc-solver-log")
        print(json.dumps(frag))
        print("```")
    return 0


def summarize(run_id=None, issue_id=None, log_records=None, commit=None,
              scan_keywords=True, day=None):
    if issue_id:
        runs = api_get(f"/api/issues/{issue_id}/runs")
        print("Runs for issue {0}:".format(issue_id))
        for r in runs:
            print("  {0}  {1}  {2}  agent {3}".format(
                r.get("id") or r.get("runId") or "?", r.get("status"),
                r.get("startedAt"), r.get("agentId") or r.get("agent") or "?"))
        if run_id is None:
            print("\nPass --run-id above to audit a run's full evidence.")
            return 0

    if log_records is None:
        if not run_id:
            raise SystemExit("--run-id is required (or use --issue-id to list runs)")
        try:
            meta = api_get(f"/api/heartbeat-runs/{run_id}")
        except HTTPError as err:
            raise SystemExit(f"run metadata failed HTTP {err.code}: {err.reason}")
        except URLError as err:
            raise SystemExit(f"run metadata failed: {err.reason}")
        log_bytes = meta.get("logBytes", 0)
        print("\nRun metadata:")
        for k in ("id", "status", "startedAt", "endedAt", "agentId", "taskId", "logBytes"):
            if k in meta:
                print("  {0}: {1}".format(k, meta[k]))
        records, fetched = fetch_log(run_id)
    else:
        log_bytes = 0
        records = log_records

    tools = Counter()
    reads, writes, edits, bash = [], [], [], []
    urls = set()
    web_tools = 0
    for chunk in iter_tool_chunks(records):
        part = chunk.get("part", {})
        tool = part.get("tool")
        tools[tool] += 1
        if tool in ("webfetch", "web_search", "fetch", "browser"):
            web_tools += 1
        inp = (part.get("state") or {}).get("input") or {}
        text = json.dumps(inp)
        urls |= external_urls(text)
        fp = inp.get("filePath")
        if tool == "read" and fp:
            reads.append(fp)
        elif tool in ("write", "edit") and fp:
            (writes if tool == "write" else edits).append(fp)
        elif tool == "bash":
            cmd = (inp.get("command") or "").strip().replace("\n", " ")
            bash.append(cmd[:200])

    print("\nTool calls ({0}):".format(sum(tools.values())))
    for tool, n in tools.items():
        print("  {0}: {1}".format(tool, n))
    if web_tools:
        print("  !! {0} web/search tool invocations".format(web_tools))
    if urls:
        print("\nExternal URLs referenced (review for contamination):")
        for u in sorted(urls):
            print("  {0}".format(u))
    else:
        print("\nNo external URLs referenced in tool inputs.")

    print("\nFiles read ({0}):".format(len(reads)))
    for fp in reads:
        print("  {0}".format(fp))
    print("\nFiles written ({0}):".format(len(writes)))
    for fp in writes:
        print("  {0}".format(fp))
    print("\nFiles edited ({0}):".format(len(edits)))
    for fp in edits:
        print("  {0}".format(fp))
    print("\nBash commands ({0}):".format(len(bash)))
    for cmd in bash:
        print("  {0}".format(cmd))

    if scan_keywords:
        hits = keyword_hits(records, day=day)
        if hits:
            print("\nKeyword scan hits (triage each: solution-source signal or benign):")
            for kw, source, snippet in hits:
                print("  [{0}] {1}: ...{2}...".format(kw, source, snippet))
        else:
            print("\nKeyword scan: no solution-source keyword hits.")

    binding_ok = True
    if commit:
        full_hits, short_only = commit_binding(records, commit)
        print("\nCommit binding (--commit {0}):".format(commit[:7]))
        print("  full-hash occurrences in decoded log: {0}".format(full_hits))
        print("  short-hash occurrences (word-boundary; weaker signal): {0}".format(
            short_only))
        if full_hits > 0:
            print("  Reviewed commit bound to this run: yes")
        else:
            print("  Reviewed commit bound to this run: NO (not found in the log)",
                  file=sys.stderr)
            binding_ok = False

    tool_calls = sum(tools.values())
    if run_id is None:
        print("\n(--log-file mode: no provenance fragment for a non-fetched run.)")
        return 0 if binding_ok else 1
    fragment = {
        "runId": run_id,
        "logBytes": log_bytes,
        "toolCalls": tool_calls,
        "complete": True,
    }
    print("\nFragment for provenance.solverRuns[]:")
    print("```aoc-solver-log")
    print(json.dumps(fragment))
    print("```")
    if not tool_calls or log_bytes <= 0:
        print("\nWARNING: empty tool calls or zero log bytes; treat coverage as unknown.",
              file=sys.stderr)
        return 1
    if not binding_ok:
        print("WARNING: --commit supplied but not found in the decoded log.",
              file=sys.stderr)
        return 1
    return 0


def main(argv=None):
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--run-id", help="solver heartbeat run id")
    parser.add_argument("--issue-id", help="issue id to list runs; audit a run with --run-id")
    parser.add_argument("--log-file", help="scan a saved redacted log excerpt instead of fetching")
    parser.add_argument("--no-keyword-scan", action="store_true",
                        help="disable the decoded-log keyword contamination scan")
    parser.add_argument("--find-solver", action="store_true",
                        help="locate the solve issue's run bound to --commit")
    parser.add_argument("--year", type=int, help="year for --find-solver")
    parser.add_argument("--day", type=int,
                       help="day for --find-solver or to scope the keyword scan")
    parser.add_argument("--part", type=int, help="part for --find-solver")
    parser.add_argument("--commit", metavar="FULL_SHA",
                        help="reviewed solution commit to bind to this run")
    args = parser.parse_args(argv)

    if args.find_solver:
        if not (args.year and args.day and args.part and args.commit):
            parser.error("--find-solver requires --year --day --part --commit")
        return find_solver_run(args.year, args.day, args.part, args.commit)

    if args.log_file:
        try:
            with open(args.log_file) as fh:
                data = json.load(fh)
        except (OSError, ValueError) as err:
            raise SystemExit(f"could not read log file {args.log_file}: {err}")
        content = data.get("content", "") if isinstance(data, dict) else data
        records = []
        for line in content.splitlines():
            line = line.strip()
            if not line:
                continue
            try:
                records.append(json.loads(line))
            except ValueError:
                continue
        return summarize(issue_id=args.issue_id, log_records=records,
                         commit=args.commit, day=args.day,
                         scan_keywords=not args.no_keyword_scan)

    if not args.run_id and not args.issue_id:
        parser.error("provide --run-id, --issue-id, or --log-file")
    return summarize(args.run_id, args.issue_id, commit=args.commit,
                     scan_keywords=not args.no_keyword_scan, day=args.day)


if __name__ == "__main__":
    sys.exit(main())
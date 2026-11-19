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
  --no-keyword-scan      Disable the decoded-log keyword contamination scan.

Environment (auto-injected): PAPERCLIP_API_URL, PAPERCLIP_API_KEY.

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
  confirm the scan reports exactly those hits. Failure cases: missing/invalid
  run id, empty or unreadable log (exit non-zero).
"""

import argparse
import json
import os
import re
import sys
import urllib.request
from collections import Counter
from urllib.error import HTTPError, URLError

API = os.environ.get("PAPERCLIP_API_URL", "").rstrip("/")
KEY = os.environ.get("PAPERCLIP_API_KEY", "")
MAX_CALL = 1024 * 1024  # log endpoint max 1 MiB per call
ALLOWED_API_HOSTS = {"paperclip.tritsch.org", "paperclip.ing"}


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
KEYWORDS = (
    r"reddit\.com",
    r"stackoverflow",
    r"leetcode",
    r"codeforces",
    r"adventofcode-solutions",
    r"aoc-solutions",
    r"answer list",
    r"spreadsheet of answers",
    r"github\.com/[^'\"]*/[^'\"]*day[_-]?0?2",
    r"solution for day 2",
    r"day 2 solution",
)


def keyword_hits(records, limit=200):
    """Return (keyword, source_label, snippet) hits from decoded tool inputs
    and text chunks. `source_label` is the tool name or "text"."""
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
        for kw in KEYWORDS:
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


def summarize(run_id=None, issue_id=None, log_records=None, scan_keywords=True):
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
        hits = keyword_hits(records)
        if hits:
            print("\nKeyword scan hits (triage each: solution-source signal or benign):")
            for kw, source, snippet in hits:
                print("  [{0}] {1}: ...{2}...".format(kw, source, snippet))
        else:
            print("\nKeyword scan: no solution-source keyword hits.")

    tool_calls = sum(tools.values())
    if run_id is None:
        print("\n(--log-file mode: no provenance fragment for a non-fetched run.)")
        return 0
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
    return 0


def main(argv=None):
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--run-id", help="solver heartbeat run id")
    parser.add_argument("--issue-id", help="issue id to list runs; audit a run with --run-id")
    parser.add_argument("--log-file", help="scan a saved redacted log excerpt instead of fetching")
    parser.add_argument("--no-keyword-scan", action="store_true",
                        help="disable the decoded-log keyword contamination scan")
    args = parser.parse_args(argv)

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
                         scan_keywords=not args.no_keyword_scan)

    if not args.run_id and not args.issue_id:
        parser.error("provide --run-id, --issue-id, or --log-file")
    return summarize(args.run_id, args.issue_id,
                     scan_keywords=not args.no_keyword_scan)


if __name__ == "__main__":
    sys.exit(main())
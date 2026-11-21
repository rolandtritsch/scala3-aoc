# Repository guidance

## Structure

- `0000/` is the reusable template, with the `aoc0000` package.
- `YYYY/` contains one year's independent sbt project and `aocYYYY` package.
- Each project keeps solutions under `src/main/scala/aocYYYY`, shared
  utilities under `src/main/scala/util`, inputs under
  `src/main/resources/inputs`, and tests under `src/test/scala`.
- `problems/` holds puzzle descriptions. Keep Day00 as the runnable example.
- Build configuration and tool versions belong to each year's directory.
- `scripts/` holds reusable workflow helpers. `skills/` holds distilled
  workflow skills with their own `SKILL.md`.

## Workflow helpers

The company package owns verification logic. Set `AOC_COMPANY_PACKAGE_DIR` to
its deployed root (default `/paperclip/company-packages/pcc-aoc-solver-scala`).
Runtime skill-loader directories may omit scripts. Inspect existing bundled
helpers before creating a new one; these checkout entry points only delegate.

- `scripts/verify_review_block.py --issue-file /scratch/issue.json
  --body-file /scratch/report.md` checks identity, the submitter receipt's full
  solution commit, and explicit solver-log provenance coverage. Fetch the full
  issue JSON first. Complete the actual tool-log audit before declaring clearance.
- `scripts/verify_review_evidence.py --year YYYY --day NN --part P
  --answer ANSWER --commit FULL_SHA` delegates to the publisher's verify mode:
  formatting, active assigned-day tests, exact runner answer and clean
  HEAD/tracking/live-remote equality before and after checks. Commit is required;
  there is no skip-tests option. Run before making review improvements.
- `scripts/extract_review_provenance.py --run-id RUN_ID [--issue-id ISSUE_ID]
  [--commit FULL_SHA]`
  pulls a solver run's evidence from the authenticated API: run metadata,
  logBytes, parsed tool-call counts, files touched, bash commands, any
  external URLs, and a decoded-log keyword scan for solution-source signals.
  `--issue-id` alone lists the issue's runs so you can pick the solver run;
  `--log-file FILE` scans a saved redacted log excerpt offline (no fragment).
  `--commit FULL_SHA` binds the audited run to the reviewed commit (searches
  tool inputs/outputs for the full hash) and exits non-zero when the reviewed
  commit is absent from the log, so a run that did not produce/publish it
  cannot clear provenance silently. API credentials are resolved from
  `AOC_PAPERCLIP_API_URL`/`AOC_PAPERCLIP_API_KEY` when present (the
  host-verified pair in bridge/queue runs) and fall back to the standard
  `PAPERCLIP_API_URL`/`PAPERCLIP_API_KEY`, so no manual export is needed.
  It prints a fenced JSON fragment for the `provenance.solverRuns[]` entry of
  the aoc-review-result block. It only reads; the reviewer still inspects the
  log to complete the contamination audit.
  `--find-solver --year YYYY --day NN --part P --commit FULL_SHA` automates
  the initial lookup: it locates this company's `AoC YYYY DayNN partP: solve`
  issue, audits every succeeded run for the reviewed commit, and prints the
  provenance fragment for the run(s) that reference it (exits non-zero when
  none binds). Use it to avoid hand-grepping issue titles and trying runs one
  by one.
- For noncomputational finales, follow the authenticated completion checks in
  the company skills instead of inventing code or a publication receipt.

## Making changes

Use Scala 3 indentation syntax and keep parsing, part 1, and part 2 separate.
Preserve strict compiler warnings. Carry relevant template fixes into existing
years deliberately; the year projects do not depend on `0000`.

README.md documents usage; this file documents maintenance and verification.
Do not copy the source repository's demo reset or company automation into
the template.

## Verification

Run commands inside the affected year directory. For the template:

```bash
cd 0000
sbt --batch 'scalafmtAll; scalafmtSbt'
sbt --batch 'clean; compile; testOnly *'
sbt --batch 'scalafmtCheckAll; scalafmtSbtCheck; run'
```

Confirm the test output reports executed tests and zero failures. Use
`testOnly *` because the pinned sbt thin client can select `testQuick` for
`test` without executing tests.

Before handing off changes, run `git diff --check` from the repository root.

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

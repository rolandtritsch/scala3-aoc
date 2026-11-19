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

- `scripts/verify_review_block.py` validates a review's fenced
  `aoc-review-result` JSON block (missing/bad/mismatched values) before the
  review issue is marked done. Pipe the comment body to stdin and pass
  `--issue-id` (review issue UUID), `--title` (issue title), and optional
  `--commit` (reviewed solution commit). Exit 0 only on a valid block.
- `scripts/verify_review_evidence.py` re-checks the mechanical review evidence
  read-only: clean tree, HEAD == origin/main, reviewed commit == HEAD, the
  year's `sbt --batch 'testOnly *'`, and the exact `DayNN - partP:` runner
  answer. Pass `--year`, `--day`, `--part`, `--answer`, optional `--commit`,
  `--repo` (defaults to `AOC_REPO_DIR`), and `--skip-sbt` to skip the slow
  sbt checks. Exit 0 only when all checks pass.

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

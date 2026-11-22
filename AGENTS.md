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

## Company execution

The current company has CTO, Gatherer, Solver, and Submitter roles. There is
no Reviewer, source audit, or algorithm assessment. Keep the instruction to
solve from first principles without claiming independently audited provenance.

Executable company helpers arrive as the complete imported `aoc-runtime`
skill. Use `AOC_RUNTIME_DIR` and the assigned `AOC_REPO_DIR`; the image has
no baked company package. Publish with:

```bash
python3 "$AOC_RUNTIME_DIR/scripts/entry.py" publish --answer ANSWER --files YEAR/path ...
```

Only CTO assigns stages and advances progress. `.aoc/owner.json` binds the
repository to one company generation. `.aoc/progress.json` records verified
per-part completion against the authenticated AoC account. `LAST.md` is
historical and must not control selection. The workspace is a cache, never
an authority for acceptance or published state.

The helper verifies formatting, positive executed assigned-day tests,
full-year tests, exact runner output, and publication. It records the result
before closing the stage; notification occurs after releasing the mutation
lock. Never create a downstream issue or advance the index manually.

Legacy review/provenance scripts under `scripts/` are historical tools, not
required company stages. Do not run them to gate progress. For finales, follow
only the authenticated site's actual interaction; never invent an answer.

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
sbt --server --batch 'scalafmtAll; scalafmtSbt'
sbt --server --batch 'clean; compile; testOnly *'
sbt --server --batch 'scalafmtCheckAll; scalafmtSbtCheck; run'
```

Confirm the test output reports executed tests and zero failures. Use
`testOnly *` because the pinned sbt thin client can select `testQuick` for
`test` without executing tests.

Before handing off changes, run `git diff --check` from the repository root.

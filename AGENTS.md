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

## Coding guardrails

These rules apply to all new and changed solution code (`DayNN.scala`, `Main.scala`,
and their tests), in every `YYYY/` project and the `0000/` template.

### ZIO is mandatory for effects

- Every entrypoint is `object Main extends ZIOAppDefault`. Do not write a plain
  `@main def solve()`.
- File IO goes through a ZIO effect, not `scala.io.Source` wrapped in
  `try`/`finally`. Acquire and release the `Source` with
  `ZIO.acquireReleaseWith`.
- Recoverable failures (a missing resource, a malformed input line, an
  unrecognized instruction, an empty secret key) are typed values, not thrown
  exceptions. Effectful code fails the ZIO error channel (`ZIO.fail`,
  `ZIO.fromEither`); pure code that can fail returns `Either`/`Option`. Define
  one `AocError` enum per `YYYY` project (see `AocError.scala`) and use it as
  the error type everywhere a day can fail.
- `require(...)` is reserved for invariants that only a bug in this code could
  violate (a value already guaranteed by its caller's contract), never for
  validating puzzle input or file contents. If the condition depends on what
  is in the input file, it is a typed error, not a `require`.
- Tests use `zio-test` (`ZIOSpecDefault`, `assertTrue`), not `munit`. A test
  that calls `readFile` runs it as a ZIO effect inside the test body; it does
  not call it as a plain synchronous function.

### Favor functions and ADTs over classes and methods

- Prefer a plain function operating on a value to a method on a class.
  Reach for a class only when wrapping genuinely stateful, effectful resources
  (and even then, prefer threading that state through ZIO).
- Model data with `enum`, `case class`, and `opaque type`, not with classes
  that hide mutable fields behind methods. `parse`-style functions return the
  parsed ADT (or `Either[AocError, ADT]`), not a class instance built by a
  constructor with side effects.
- Keep `parse`, `part1`, and `part2` pure and total once their input is
  already validated; push IO and validation to `readFile`.

### No `var`; prefer tail recursion

- Do not declare `var`. Replace counter/accumulator loops with
  `@scala.annotation.tailrec` recursion, `foldLeft`, or a `for`-comprehension
  over a range, whichever reads clearest.
- Use tail recursion where it is a net improvement: it avoids a `var` without
  regressing readability or performance. Where a `for`-comprehension over a
  range already expresses the same iteration with no `var`, prefer that
  instead of hand-rolling a recursive helper.
- A local mutable `Array` used as an O(1)-space accumulator (e.g. a day's
  pixel/brightness grid) is acceptable — it is not a `var` — but iterate it
  with a `for`-comprehension or recursion, never a `while` loop driven by
  `var` counters.
- The shared `util/` framework (`Grid`, `Bfs`, `Dfs`, `GridGraph`,
  `WDGridGraph`) predates these guardrails and no current day depends on it.
  Bring a utility in line with this section only once a day actually starts
  using it; this is not a mandate to refactor `util/` up front.

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

# Repository guidance

## Structure

- `0000/` is the reusable template, with the `aoc0000` package.
- `YYYY/` contains one year's independent sbt project and `aocYYYY` package.
- Each project keeps a day's solutions under `src/main/scala/aocYYYY/dayNN`,
  inputs under `src/main/resources/inputs`, and tests under
  `src/test/scala/aocYYYY/dayNN`.
- Each `dayNN` package contains `Part1.scala`, `Part2.scala`, `ReadFile.scala`,
  and any other implementation files needed for that day.
- Implementation used by more than one day belongs in that project's
  `aocYYYY.util` package, including `AocError` and resource loading. Each year
  remains independent; the template uses `aoc0000.day00` and `aoc0000.util`.
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

These rules apply to all new and changed solution code (day packages, `util`,
`Main.scala`, and their tests), in every `YYYY/` project and the `0000/` template.

### Organize implementations by day and part

- Give every day a zero-padded `aocYYYY.dayNN` package and matching directory.
  Do not put a monolithic `DayNN` object in the year's root package.
- Put the pure `part1` function and its exclusive helpers in `Part1.scala`;
  put `part2` and its exclusive helpers in `Part2.scala`. Prefer package-level
  functions over wrapper objects that only provide a namespace.
- Every day has `ReadFile.scala`, exposing its typed ZIO `readFile` function.
  Keep domain types, pure parsing, and helpers shared by that day's parts in
  appropriately named files within the same `dayNN` package.
- Keep the ZIO application entrypoint in the year's `Main.scala`. Read and
  validate each day's input once, then pass that value to both pure parts;
  the part functions do not load their own files.
- Move implementation used across day boundaries into `aocYYYY.util`. Days
  must not import implementation from other day packages. Keep implementation
  used by only one day in that day's package; extract actual shared code rather
  than speculative abstractions.
- Match test packages and directories to their implementation packages. Preserve
  domain validation when splitting files: constrained constructors and generated
  `copy` methods must remain inaccessible to callers that could bypass it.

### End markers follow formatted function length

- Named functions (`def`) spanning six or more formatted lines must end with
  `end functionName`. Functions spanning five or fewer lines must omit that
  marker. This includes local functions and methods.
- Count from the `def` keyword through the last line of its body, including
  internal blank lines and comments, but excluding annotations, documentation,
  trailing blank lines, and the function's own end marker.
- Run Scalafmt first, then `scalafixAll --check`. The local `FunctionEndMarkers`
  Scalafix rule checks every function, including expression chains and local
  functions. Add or remove a function's marker when this check reports an error.
- Scalafmt's end-marker insertion and removal are disabled: its span includes
  annotations, which can conflict with this rule. Scalafix is the authority for
  function markers; do not exempt a function because the formatter left it
  unchanged.
- Non-function markers (`end if`, `end match`, `end for`, and type/object markers)
  are outside this function-length rule. Keep them where they clarify scope.

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
  one `AocError` enum per project in `aocYYYY.util` (see `util/AocError.scala`)
  and use it as the error type everywhere a day can fail. The template uses
  `aoc0000.util.AocError`.
- `require(...)` is reserved for invariants that only a bug in this code could
  violate (a value already guaranteed by its caller's contract), never for
  validating puzzle input or file contents. If the condition depends on what
  is in the input file, it is a typed error, not a `require`.
- Tests use `zio-test` (`ZIOSpecDefault`, `assertTrue`), not `munit`. A test
  that calls `readFile` runs it as a ZIO effect inside the test body; it does
  not call it as a plain synchronous function.
- Log via `ZIO.log*` (`ZIO.logDebug` for diagnostic detail, `ZIO.logError`
  for failures), never a per-class `Logger`. `readFile` is already an
  effect, so it logs its own filename/line/parsed-value detail internally.
  `parse`, `part1`, and `part2` stay pure (see below), so their
  inputs/results are logged from the calling effect instead — typically
  `Main`, right after `readFile` succeeds and around each `part1`/`part2`
  call. Log a day's failure with `.tapError(e => ZIO.logError(...))` at the
  point it's bound into the pipeline. No `zio-logging`/`slf4j`/`logback`
  dependency is needed for this: ZIO's default console logger handles
  `ZIO.log*` out of the box, and that heavier stack belongs to a
  production-service template, not this CLI.

### Favor functions and ADTs over classes and methods

- Prefer a plain function operating on a value to a method on a class.
  Reach for a class only when wrapping genuinely stateful, effectful resources
  (and even then, prefer threading that state through ZIO).
- Model data with `enum`, `case class`, and `opaque type`, not with classes
  that hide mutable fields behind methods. `parse`-style functions return the
  parsed ADT (or `Either[AocError, ADT]`), not a class instance built by a
  constructor with side effects.
- Use `Seq` until you have a good reason to use something else. When choosing
  another collection, leave a comment in the code explaining why, including
  when laziness enables early termination.

### Parse and type input at the boundary

- Convert raw input into validated domain values in `readFile`. For a sequence
  of domain values, return `ZIO[Any, AocError, Seq[DomainValue]]`; return another
  domain type when the puzzle's structure warrants it.
- Define meaningful domain types before implementing the solution. Use enums
  for closed alternatives, case classes for structured values, and opaque types
  where distinguishing otherwise identical primitive types prevents mistakes.
- Translate input symbols into domain values immediately. For example, Day01
  returns `Seq[Move]`, with `Move.Up` and `Move.Down`, rather than parentheses
  as a `String`.
- Keep raw strings only when text itself is the domain value. Do not encode
  already-understood instructions, directions, or states as strings, characters,
  integers, or booleans.
- Validate all input-dependent assumptions before `readFile` succeeds. Neither
  `part1` nor `part2` should interpret raw syntax or discover malformed input.
- Keep parsing pure and separately testable: `parse` returns
  `Either[AocError, DomainValue]`. Lift its result into ZIO inside `readFile`,
  which owns file IO and orchestrates validation.
- Report invalid input through `AocError`, with the filename and useful line or
  character position. Never silently discard an unrecognized instruction.
- Handle whitespace deliberately according to the input format. Ignore it only
  when it has no domain meaning.

### Keep solution functions pure and total

- `part1` and `part2` accept validated domain values and return explicitly typed
  answers. They perform no IO, logging, or raw-input parsing.
- Match domain enums exhaustively. Avoid wildcard cases that hide an unhandled
  domain alternative.
- Represent legitimate "no result" outcomes explicitly, normally with `Option`.
  Use a sentinel such as `-1` only when it is an intentional, documented answer
  contract. Use `Either[AocError, Answer]` for recoverable computation failures.
- Avoid partial operations such as unchecked indexing, `.get`, `.head`, or
  `.last` unless the function's construction or validated input guarantees they
  are safe.

### Express the algorithm directly

- Prefer transformations such as `map`, `foldLeft`, `scanLeft`, and `indexWhere`
  when they express the algorithm clearly.
- Use `foldLeft` when only the final accumulated value is needed; use `scanLeft`
  when intermediate states are needed.
- Extract a small, pure transition function when both parts apply the same
  domain rules. Share domain logic without forcing both parts into an unsuitable
  algorithm; do not require every puzzle to use Day01's `scanLeft` approach.
- Derive positions from sequence structure where possible. Avoid separate
  counters and flags when an index or search operation already expresses the
  result.
- Prefer searches that stop once the answer is known. Avoid guards that suppress
  updates while still traversing the remaining input.
- Avoid unnecessary intermediate collections and repeated computations while
  keeping the implementation readable.

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
- Put a grid, graph, or search structure in its day's package while only that
  day uses it. Move it to `util` when it is used across day boundaries, following
  the same guardrails.
- `no var`/`no while`/`no throw`/`no null`/`no return` are machine-enforced
  via scalafix's built-in `DisableSyntax` rule (configured in
  `.scalafix.conf`), not just documented here. WartRemover was evaluated as
  an alternative but has no published build compatible with this repo's
  pinned sbt 2.0.x line (only an old `2.0.0-M4` milestone exists); revisit if
  that changes. Run `sbt scalafixAll --check` to verify.
- `.scalafix.conf` also loads a handful of `io.github.migiside/zio-scalafix`
  rules (`PreferZIOAs`, `PreferZIOOrElseFail`, etc.) that rewrite common
  non-idiomatic ZIO patterns. That project isn't published to Maven Central,
  so the rules are referenced as raw source URLs pinned to a commit SHA —
  scalafix compiles them at lint time. Bump the SHA deliberately; don't
  float on a branch.

## Verification

- Test parsing separately from solving: domain mapping, malformed input,
  permitted whitespace, and boundary cases.
- Preserve official examples and real-input answer checks.
- Explain non-obvious invariants in code comments, such as why a sequence index
  equals an instruction number.
- Keep performance comments precise: sharing a helper shares code; it does not
  necessarily share a computed result.

Run commands inside the affected year directory. For the template:

```bash
cd 0000
sbt --server --batch 'scalafmtAll; scalafmtSbt'
sbt --server --batch 'clean; compile; testOnly *'
sbt --server --batch 'scalafmtCheckAll; scalafmtSbtCheck; run'
sbt --server --batch 'scalafixAll --check'
```

The end-marker rule is shipped in each project's `.scalafix/` directory so
copied templates and year projects can lint independently. Local rule sources
use the Scala 2 syntax required by Scalafix's source compiler. To verify its
boundary cases from the repository root, run
`python3 scripts/test_function_end_markers.py YEAR`.

Confirm the test output reports executed tests and zero failures. Use
`testOnly *` because the pinned sbt thin client can select `testQuick` for
`test` without executing tests.

Before handing off changes, run `git diff --check` from the repository root.

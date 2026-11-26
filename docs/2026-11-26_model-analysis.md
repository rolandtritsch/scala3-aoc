# Model comparison: deepseek (Day07–16) vs nvidia (Day18–21)

Date: 2026-11-26  
Repository: `scala3-aoc`, project `2015/`

## Scope and method

| Item | Decision |
| --- | --- |
| Batch 1 (deepseek) | `2015/src/main/scala/aoc2015/day07` … `day16` and matching tests |
| Batch 2 (nvidia) | `2015/src/main/scala/aoc2015/day18` … `day21` and matching tests |
| Excluded | Day00 (template), Day01–06 (heavily rewritten by the human ZIO/Scalafix refactor), Day17 (author unknown), shared `util/`, `Main.scala`, inputs |
| Attribution source | User statement only; git history contains no model labels |
| Static checks | `scalafmtCheckAll`, `scalafmtSbtCheck`, `scalafixAll --check`, `clean; compile; testOnly *` |
| Speed | Timestamped stdout of repeated full `sbt run` with an sbt server already warm; per-day deltas between printed answers |

All checks passed: formatting, Scalafix (including the function-end-marker rule), compilation with `-Werror`, and **250 executed tests / 0 failures / 0 ignored**.

## Executive summary

**Verdict: deepseek is the better model here, but only narrowly and with modest confidence.** It produces essentially the same amount of code per day (≈90 source LOC/day), but its solutions are more consistently idiomatic, its tests actually verify shipped behavior, and its median per-day runtime is lower. Nvidia's code is clean at the file level and equally small, but it has more total-answer shortcuts (`-1`, `.get`), a test suite that reimplements rather than exercises the production algorithm (Day18), and weaker/misleading tests (Day20, Day21).

The honest caveat: 10 days vs 4, different puzzle difficulty, and stricter/clearer instructions by Day18–21. This is a code-quality comparison, not a controlled model benchmark.

## Scorecard

| Criterion | deepseek (Day07–16) | nvidia (Day18–21) | Winner |
| --- | --- | --- | --- |
| Less code | 908 source LOC / 684 test LOC over 10 days (90.8 + 68.4 per day) | 356 source LOC / 285 test LOC over 4 days (89.0 + 71.3 per day) | **Tie** |
| Faster code | Median 0.076 s/day; one outlier (Day10, 4.23 s) | Median 0.380 s/day; two heavy days (Day18 2.33 s, Day20 0.73 s) | **deepseek** (weak: puzzles differ) |
| Easier/better to read | Descriptive names, rich Scaladoc, but over-documented parser in Day12 | Small, focused files and clear ADTs, but stale/defensive comments in Day20 | **deepseek** slightly |
| Idiomatic Scala 3/ZIO | Pure parts, typed `Either`, opaque types/enums, `Either` answers, exhaustive matches | Pure parts and good opaque types, but `-1` sentinel, `.get`, raw syntax in `part2`, shared helpers in `Part1.scala` | **deepseek** |
| Correctness/robustness | 102 tests, every day has malformed-input coverage; but JSON trailing commas and negative values slip through | 35 tests, only Day18/Day19 cover malformed input; Day18 example tests test a clone | **deepseek** |
| Test independence | Tests assert fixed expected values against real functions | Day18 duplicates the simulator in the test; Day21 asserts `part1(input) > 0` | **deepseek** clearly |

## Objective metrics

Source lines are total lines; “comment” counts comment-only lines. Test counts are from the executed ZIO test report, not source grep.

| Day | Source LOC | Comment lines | Test LOC | Tests | Median s |
| ---: | ---: | ---: | ---: | ---: | ---: |
| 07 | 235 | 46 | 114 | 13 | 0.036 |
| 08 | 133 | 63 | 81 | 12 | 0.013 |
| 09 | 114 | 45 | 52 | 8 | 0.179 |
| 10 | 89 | 30 | 60 | 10 | 4.233 |
| 11 | 120 | 47 | 61 | 10 | 0.272 |
| 12 | 288 | 36 | 81 | 10 | 0.026 |
| 13 | 146 | 69 | 55 | 8 | 0.116 |
| 14 | 127 | 50 | 80 | 10 | 0.015 |
| 15 | 173 | 75 | 71 | 11 | 0.287 |
| 16 | 222 | 74 | 72 | 10 | 0.032 |
| **deepseek total** | **1647** | **535** | **684** | **102** | **5.208** |
| 18 | 131 | 22 | 123 | 10 | 2.328 |
| 19 | 131 | 12 | 85 | 10 | 0.013 |
| 20 | 104 | 30 | 69 | 7 | 0.730 |
| 21 | 146 | 15 | 66 | 8 | 0.030 |
| **nvidia total** | **512** | **79** | **285** | **35** | **3.101** |

Per day: source LOC 164.7 vs 128.0, but code-only LOC 90.8 vs 89.0 — the size difference is almost entirely comments and blank lines, not logic. Deepseek averages 53.5 comment lines/day; nvidia 19.8.

Guardrail scan of solution code (tests excluded): **neither batch uses `var`, `while`, `throw`, `return`, `null`, `require`, or a per-class logger**; all part functions are pure and all `readFile`s are ZIO effects. Remaining partial operations: deepseek 2 `.get`, 2 `.head`, 2 `.last`, 1 `null`; nvidia 2 `.get`, 1 `.head`. Deepseek parts return `Either` where no answer is possible; nvidia uses `-1` once.

## Good, bad, ugly

### deepseek (Day07–16)

**Good**

- Closed-form reasoning instead of brute force: `2015/src/main/scala/aoc2015/day14/Part1.scala:13` computes distance in O(1), reused by part 2 at `.../day14/Part2.scala:12`.
- Memoized circuit evaluation with 16-bit masking and duplicate-wire rejection at the boundary: `.../day07/Part1.scala:36`, `.../day07/ReadFile.scala:15`.
- Typed decoder with line-position errors and allocation-free length arithmetic: `.../day08/StringLiteral.scala:47`, `.../day08/Part2.scala:18`.
- A real ADT boundary: `.../day16/Attribute.scala:12` (enum with `fromLabel`), and parts returning `Either[AocError, Int]` via early-terminating `find` at `.../day16/Part1.scala:11` and `.../day16/Part2.scala:14`.
- Small, separately named pure predicates for the password search: `.../day11/Password.scala:62`, `:66`, `:72`, `:82`.
- Tests verify shipped behavior with official examples, real answers, malformed input and boundary cases; e.g. `2015/src/test/scala/aoc2015/day12/Day12Test.scala:25` and `.../day16/Day16Test.scala:9-68`. The compile-time `copy` guard at `.../day14/Day14Test.scala:11` is a nice touch.

**Bad**

- Input-dependent exceptions still reachable: `.../day07/Part1.scala:36` calls `env(name)` directly for undefined wires, and `.../day13/Preference.scala:45` uses `amount.toInt` on unbounded digits.
- Under-validation: negative shift amounts accepted at `.../day07/Instruction.scala:53`; negative Sue counts accepted at `.../day16/AuntSue.scala:82` despite the “non-negative” message.
- JSON trailing commas are accepted (`[1,]`, `{"a":1,}`) at `.../day12/Json.scala:186` and `:220`; the string loop at `:92` is not tail-recursive.
- Public case-class constructor at `.../day13/Preference.scala:19` breaks the private-constructor discipline used elsewhere; `case _` over a closed enum at `.../day16/Part2.scala:28` hides future additions.

**Ugly**

- `.../day12/Json.scala` is 232 lines for a small parser, with Scaladoc on nearly every private helper (e.g. `:128`, `:171`); control flow is harder to see than the code warrants.
- `.../day12/Part2.scala:26` packs two match cases onto one line with `;`.
- `.../day15/Recipe.scala:17` documents `count >= 1` but does not enforce it; `compositions(0, n)` never terminates.

**Performance**

- Day10 dominates: `.../day10/Digits.scala:49` uses `init :+ (...)` per digit and another Vector flat-map, allocating heavily on the way to ~7M digits (4.23 s here).
- Day09 (`.../day09/Routes.scala:15`), Day13 (`.../day13/Seating.scala:35`) and Day15 (`.../day15/Recipe.scala:16`) eagerly enumerate all permutations/compositions (8!, 7!, 176,851) and both parts redo the work.
- Day14 and Day16 are the opposite: O(1) distance and early-terminating `find`.

### nvidia (Day18–21)

**Good**

- Clean boundary types: opaque `Light` with `Option`/`Either` constructors (`2015/src/main/scala/aoc2015/day18/Light.scala:4-26`), opaque `Input` (`.../day20/Input.scala:6-17`), and enums/case classes in Day19 and Day21 (`.../day19/Replacement.scala:4`, `.../day21/ItemType.scala:4-5`, `.../day21/FightResult.scala:4-5`).
- Strong `readFile` validation, especially Day18 row-count checks (`.../day18/ReadFile.scala:12-21`) and Day19's one-molecule/all-rules checks (`.../day19/ReadFile.scala:10-19`).
- Pure, explicitly typed part functions throughout; tail-recursive combat with shared rules in `.../day21/Shop.scala:4-18`; correct “at most one ring of each kind” via `combinations` at `:44-55`.
- Day18's malformed-readFile test properly exercises the ZIO error channel (`2015/src/test/scala/aoc2015/day18/Day18Test.scala:38-44`).

**Bad**

- Tests reimplement solutions: Day18's example tests call local `simulate`, `simulateWithCorners`, `countNeighborsTest`, `nextState`, `isCornerTest`, `setCornersTest` instead of `part1`/`part2` (`2015/src/test/scala/aoc2015/day18/Day18Test.scala:20-32`, `:49-53`, `:68-121`); `parseTestLine` uses `sys.error` at `:64`.
- Sentinel/partial shortcuts: `-1` for impossible BFS (`.../day19/Part2.scala:30`) and unchecked `.get` after one doubling retry (`.../day20/Part1.scala:24`, `.../day20/Part2.scala:16`).
- `part2` still interprets raw input syntax: `.../day19/Part2.scala:9-21` branches on `contains("Rn"/"Ar"/"Y")` and parses element symbols inside the part function, with no explanation of the count formula.
- Cross-part helpers live in `Part1.scala` (`.../day18/Part1.scala:22-39`) but are used by `Part2.scala:16-18`.
- Weak or misleading tests: `2015/src/test/scala/aoc2015/day21/Day21Test.scala:21-29` only asserts `part1(input) > 0` with an apology comment; `.../day20/Day20Test.scala:55-59` is titled “target 130 -> house 6” but asserts 8; Day19's `parseExample` silently drops malformed rules (`.../day19/Day19Test.scala:75-83`). Only Day18 has a malformed-`readFile` test.
- Stale/contradictory comments: “Placeholder for Part 2 solution” on a complete implementation (`.../day20/Part2.scala:3-7`) and the meandering bound rationale at `.../day20/Part1.scala:35-43`.

**Ugly**

- Day18's hard-coded `gridSide` and `steps = 100` (`.../day18/Grid.scala:3`, `.../day18/Part1.scala:7`) make the official 6×6 examples impossible to run through `part1`/`part2`, so the test suite carries ~54 lines of duplicate simulator code.
- Day20's bound strategy is an unjustified initial guess, full sieve recomputation, and `.get` (`.../day20/Part1.scala:16-25`, `.../day20/Part2.scala:10-17`).
- Day19 runs two unrelated algorithms selected by substring heuristics, with an unbounded fallback BFS that is never exercised on the real input (`.../day19/Part2.scala:6-47`).

**Performance**

- Day18: 100 generations × 10,000 cells × 8 neighbours; `countNeighbors` builds two Ranges, filters, yields `1`, then calls `.length` per cell (`.../day18/Part1.scala:8-17`, `:22-28`), then `flatten.count` copies the grid (`:18`, `Part2.scala:23`). This is the batch's 2.33 s outlier.
- Day20: Θ(n log n) sieve with O(n) `Array[Int]`, plus ~50n updates in part 2; `zipWithIndex.drop(1).find` allocates a tuple per house where `indexWhere` would do (`.../day20/Part1.scala:19-24`, `.../day20/Part2.scala:13-16`). `i * limit` can overflow `Int` at `Part2.scala:25`.
- Day19 part 1 allocates substrings per rule/index (`.../day19/Part1.scala:11-18`); BFS uses `rest ++ nextStates` and `visited ++ ...` (`.../day19/Part2.scala:34-43`), but the real input takes the analytical path. Day21's 660 combinations are negligible.

## Batch 1 vs Batch 2: how they are similar and how they differ

**Similarities**

- Same architecture: `Part1.scala`, `Part2.scala`, `ReadFile.scala`, day-level domain files, ZIO `readFile`, shared `AocError`, pure part functions, no IO/logging inside parts.
- Same guardrail compliance: no `var`, `while`, `throw`, `return`, `null`, `require` in solution code; clean `scalafmt`/`scalafix`; explicit end markers.
- Nearly identical code volume per day (90.8 vs 89.0 code LOC) and similar test volume per day (68.4 vs 71.3 test LOC).
- Both use ADTs at the boundary (enums, case classes, opaque types) and both have at least one allocation-heavy day (Day10 vs Day18) and one brute-force enumeration day (Day09/Day13 vs Day21).
- Neither layer of shared code (`util/`, `Main.scala`) is attributed to either model.

**Differences**

- Documentation: deepseek writes ~2.7× as many comment lines per day (53.5 vs 19.8). Nvidia is terser but sometimes leaves stale or self-contradictory comments.
- Testing discipline: every deepseek day has parse, example, real-answer and malformed-input coverage; nvidia misses malformed-input tests on Days 19–21, duplicates Day18's algorithm in tests, and uses a `> 0` sanity check in Day21.
- Total-answer handling: deepseek returns `Either` from parts when no answer is possible (Day16); nvidia uses `-1` (Day19) and `.get` after an unbounded guess (Day20).
- Helper placement and algorithmic insight: deepseek extracts genuinely shared helpers into day-level files (Day14, Day15) and uses closed forms (Day14); nvidia leaves shared Life helpers in `Part1.scala` and selects algorithms by raw-string heuristics in `part2` (Day19).
- Runtime profile: deepseek has one slow day and a low median (0.076 s); nvidia has two slow days and a high median (0.380 s), so nvidia's lower total (3.10 s vs 5.21 s) only reflects having 4 days instead of 10.

## Speed measurement notes

- Started `sbt --server` in the background, then ran four `sbt --batch 'run'` invocations with stdout timestamped by a small pipe. Discarded run 1 as warm-up and took the median of runs 2–4.
- `Main.scala:8-148` prints every day's answers sequentially, so a day's duration is the delta between consecutive final `DayNN - partN:` lines. It includes that day's `readFile` plus `part1`/`part2`, excludes sbt and forked-JVM startup, and benefits from JIT warming on later days.
- Whole-run wall time median: **14.1 s**; sum of day deltas median: **12.9 s**.
- These are end-to-end observations on different puzzles, not isolated microbenchmarks. They support “both models can write an allocation-heavy hot loop” more strongly than a blanket speed ranking.

## Caveats

1. Attribution is user-supplied; no commit or file records the generating model.
2. The batches are 10 vs 4 days with very different problem difficulty. Day12 is a hand-written JSON parser; Day20 is a divisor sieve; Day18 is a 100-step Life simulation.
3. `AGENTS.md` and the tooling rules evolved during the year, so nvidia worked from a stricter, more explicit specification than the early deepseek days.
4. Day17 is excluded; Days 01–06 are excluded because later human refactors dominate their current form.
5. Static metrics count source text, not semantic complexity; comment-heavy files inflate LOC but not necessarily difficulty.

## Final answer

**Which model is better? Deepseek, narrowly.** It wins on test quality, idiomatic totality of results, algorithmic clarity and median runtime; code size is a wash. Nvidia is a close second: its files are concise and its ADTs are clean, but the Day18 test clone, Day20 `.get`/bound heuristics, Day19 `-1` sentinel and raw-string dispatch, and Day21's weak test are exactly the kinds of shortcuts that make a solution harder to trust.

If the decision is “who should write the next day”, pick **deepseek** for discipline and verification, while borrowing nvidia's good ideas (small opaque-type boundaries, clean combat/fight ADTs). Do not treat the runtime numbers as a strong model-level signal: both batches contain one pathological hot loop, and puzzle difficulty dominates the measurement.

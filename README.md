# scala3-aoc

Solutions to [Advent of Code][aoc] in Scala 3, organized by year.

```text
scala3-aoc/
  0000/   Day00 template and reusable utilities
  YYYY/   Solutions for a specific year
```

Each year is an independent sbt project. `0000` is reserved for the template;
it contains a Fibonacci example with sample inputs and tests, plus grid,
position, graph, and search utilities. It was extracted from the initial
Day00 setup in [scala3-aoc-2025][blueprint], with subsequent build fixes.

## Run the template

Install [asdf][] and its Java and sbt plugins, then:

```bash
cd 0000
asdf install
sbt --batch 'clean; compile; testOnly *'
sbt --batch run
```

The pinned toolchain is Java `openjdk-25`, sbt `2.0.10`, and Scala `3.9.0`.
Day00 prints `55` for part 1 and the 209-digit Fibonacci number `fib(1000)`
for part 2.

Use `testOnly *` to execute every test suite explicitly: the pinned sbt thin
client can translate `test` into `testQuick` and report no tests to run.

## Start a year

From the repository root, copy the template without generated build output:

```bash
rsync -a --exclude=target --exclude=project/project --exclude=.bsp \
  --exclude=.bloop --exclude=.metals 0000/ YYYY/
```

Replace `YYYY` with the year. Rename `aoc0000` to `aocYYYY` in the source and
test directory names and Scala package declarations. Update the project name
in `YYYY/build.sbt`, then add daily solutions, inputs, and tests alongside
Day00. Run sbt from that year's directory. Add the year to the CI matrix so
its tests run on pushes and pull requests.

See [AGENTS.md][] for change and verification guidance.

[aoc]: https://adventofcode.com
[asdf]: https://asdf-vm.com
[blueprint]: https://github.com/rolandtritsch/scala3-aoc-2025/tree/8c874b5
[AGENTS.md]: AGENTS.md

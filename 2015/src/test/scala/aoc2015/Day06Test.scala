package aoc2015

import aoc2015.Day06.*
import aoc2015.Day06.Action

class Day06Test extends munit.ScalaCheckSuite:

  test("Day06 - parse - each action"):
    assertEquals(parse("turn on 0,0 through 999,999"), (Action.TurnOn, 0, 0, 999, 999))
    assertEquals(parse("turn off 499,499 through 500,500"), (Action.TurnOff, 499, 499, 500, 500))
    assertEquals(parse("toggle 0,0 through 999,0"), (Action.Toggle, 0, 0, 999, 0))

  test("Day06 - parse - multi-digit coordinates"):
    assertEquals(parse("toggle 322,558 through 977,958"), (Action.Toggle, 322, 558, 977, 958))
    assertEquals(parse("turn on 1,2 through 3,4"), (Action.TurnOn, 1, 2, 3, 4))

  test("Day06 - part1 - single cell"):
    assertEquals(part1(Seq("turn on 0,0 through 0,0")), 1)
    assertEquals(part1(Seq("toggle 0,0 through 0,0")), 1)
    assertEquals(part1(Seq("turn off 0,0 through 0,0")), 0)

  test("Day06 - part1 - official samples, applied in order"):
    val samples = Seq(
      "turn on 0,0 through 999,999",
      "toggle 0,0 through 999,0",
      "turn off 499,499 through 500,500",
    )
    assertEquals(part1(Seq(samples.head)), 1000000)
    assertEquals(part1(samples.take(2)), 999000)
    assertEquals(part1(samples), 998996)

  test("Day06 - part1 - empty input"):
    assertEquals(part1(Seq.empty), 0)

  test("Day06 - part1 - rectangle size"):
    assertEquals(part1(Seq("turn on 0,0 through 2,2")), 9)
    assertEquals(part1(Seq("turn on 0,0 through 0,3")), 4)

  test("Day06 - part1 - toggle twice restores state"):
    val instructions =
      Seq("turn on 0,0 through 9,9", "toggle 0,0 through 4,4", "toggle 0,0 through 4,4")
    assertEquals(part1(instructions), 100)

  test("Day06 - readFile"):
    val obtained = readFile("inputs/Day06.txt")
    assertEquals(obtained.length, 300)
    assertEquals(obtained.head, "turn off 660,55 through 986,197")

  test("Day06 - part1 - real input"):
    val input = readFile("inputs/Day06.txt")
    val obtained = part1(input)
    assert(clue(obtained) > 0)

  test("Day06 - part2 - single cell brightness"):
    assertEquals(part2(Seq("turn on 0,0 through 0,0")), 1)
    assertEquals(part2(Seq("toggle 0,0 through 0,0")), 2)
    assertEquals(part2(Seq("turn off 0,0 through 0,0")), 0)

  test("Day06 - part2 - brightness never below zero"):
    val instructions = Seq("turn off 0,0 through 0,0", "turn off 0,0 through 0,0")
    assertEquals(part2(instructions), 0)

  test("Day06 - part2 - rectangle sums"):
    val instructions = Seq("turn on 0,0 through 1,1", "toggle 0,0 through 0,1")
    assertEquals(part2(instructions), 4 + 4)

  test("Day06 - part2 - empty input"):
    assertEquals(part2(Seq.empty), 0)

  test("Day06 - part2 - real input"):
    val input = readFile("inputs/Day06.txt")
    val obtained = part2(input)
    assert(clue(obtained) > 0)

end Day06Test

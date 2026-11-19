package aoc2015

import aoc2015.Day03.*

class Day03Test extends munit.ScalaCheckSuite:

  test("Day03 - part1 - official samples"):
    assertEquals(part1(">"), 2)
    assertEquals(part1("^>v<"), 4)
    assertEquals(part1("^v^v^v^v^v"), 2)

  test("Day03 - part1 - empty input"):
    assertEquals(part1(""), 1)

  test("Day03 - part1 - single moves"):
    assertEquals(part1("^"), 2)
    assertEquals(part1("v"), 2)
    assertEquals(part1("<"), 2)

  test("Day03 - part1 - back and forth"):
    assertEquals(part1("<>"), 2)
    assertEquals(part1("^v"), 2)

  test("Day03 - part1 - square"):
    assertEquals(part1("^>v<"), 4)

  test("Day03 - part1 - real input"):
    val input = readFile("inputs/Day03.txt")
    assertEquals(part1(input), 2592)

end Day03Test

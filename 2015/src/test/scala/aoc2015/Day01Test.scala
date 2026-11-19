package aoc2015

import aoc2015.Day01.*

class Day01Test extends munit.ScalaCheckSuite:

  test("Day01 - part1 - official samples"):
    assertEquals(part1("(())"), 0)
    assertEquals(part1("()()"), 0)
    assertEquals(part1("((("), 3)
    assertEquals(part1("(()(()("), 3)
    assertEquals(part1("))((((("), 3)
    assertEquals(part1("())"), -1)
    assertEquals(part1("))("), -1)
    assertEquals(part1(")))"), -3)
    assertEquals(part1(")())())"), -3)

  test("Day01 - part1 - empty input"):
    assertEquals(part1(""), 0)

  test("Day01 - part1 - single characters"):
    assertEquals(part1("("), 1)
    assertEquals(part1(")"), -1)

  test("Day01 - part1 - real input"):
    val input = readFile("inputs/Day01.txt")
    assertEquals(part1(input), 138)

  test("Day01 - part2 - official samples"):
    assertEquals(part2(")"), 1)
    assertEquals(part2("()())"), 5)

  test("Day01 - part2 - never enters basement"):
    assertEquals(part2(""), -1)
    assertEquals(part2("("), -1)
    assertEquals(part2("(())"), -1)
    assertEquals(part2("()()"), -1)

  test("Day01 - part2 - enters basement later"):
    assertEquals(part2("(()))"), 5)
    assertEquals(part2(")())())"), 1)
    assertEquals(part2("(((())))"), -1)

  test("Day01 - part2 - real input"):
    val input = readFile("inputs/Day01.txt")
    assertEquals(part2(input), 1771)

end Day01Test

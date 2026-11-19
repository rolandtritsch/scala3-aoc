package aoc2015

import aoc2015.Day02.*

class Day02Test extends munit.ScalaCheckSuite:

  test("Day02 - part1 - official samples"):
    assertEquals(wrappingPaper((2, 3, 4)), 58)
    assertEquals(wrappingPaper((1, 1, 10)), 43)

  test("Day02 - part1 - cube"):
    assertEquals(wrappingPaper((1, 1, 1)), 7)

  test("Day02 - part1 - two equal dimensions"):
    assertEquals(wrappingPaper((2, 2, 3)), 36)

  test("Day02 - part1 - readFile"):
    val obtained = readFile("inputs/Day02.txt")
    assertEquals(obtained.length, 1000)
    assertEquals(obtained.head, (20, 3, 11))

  test("Day02 - part1 - real input"):
    val input = readFile("inputs/Day02.txt")
    assertEquals(part1(input), 1606483)

  test("Day02 - part2 - official samples"):
    assertEquals(ribbon((2, 3, 4)), 34)
    assertEquals(ribbon((1, 1, 10)), 14)

  test("Day02 - part2 - cube"):
    assertEquals(ribbon((1, 1, 1)), 5)

  test("Day02 - part2 - two equal dimensions"):
    assertEquals(ribbon((2, 2, 3)), 20)

  test("Day02 - part2 - real input"):
    val input = readFile("inputs/Day02.txt")
    assertEquals(part2(input), 3842356)

end Day02Test

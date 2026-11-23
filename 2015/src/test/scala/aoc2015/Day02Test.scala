package aoc2015

import aoc2015.Day02.*
import zio.*
import zio.test.*

object Day02Test extends ZIOSpecDefault:

  def spec: Spec[Any, Any] = suite("Day02")(
    test("part1 - official samples"):
      assertTrue(wrappingPaper((2, 3, 4)) == 58, wrappingPaper((1, 1, 10)) == 43)
    ,
    test("part1 - cube"):
      assertTrue(wrappingPaper((1, 1, 1)) == 7)
    ,
    test("part1 - two equal dimensions"):
      assertTrue(wrappingPaper((2, 2, 3)) == 36)
    ,
    test("part1 - readFile"):
      readFile("inputs/Day02.txt")
        .map(obtained => assertTrue(obtained.length == 1000, obtained.head == (20, 3, 11)))
    ,
    test("part1 - real input"):
      readFile("inputs/Day02.txt").map(input => assertTrue(part1(input) == 1606483))
    ,
    test("part2 - official samples"):
      assertTrue(ribbon((2, 3, 4)) == 34, ribbon((1, 1, 10)) == 14)
    ,
    test("part2 - cube"):
      assertTrue(ribbon((1, 1, 1)) == 5)
    ,
    test("part2 - two equal dimensions"):
      assertTrue(ribbon((2, 2, 3)) == 20)
    ,
    test("part2 - real input"):
      readFile("inputs/Day02.txt").map(input => assertTrue(part2(input) == 3842356))
    ,
    test("parseLine - rejects malformed line"):
      assertTrue(parseLine("test", "2x3").isLeft, parseLine("test", "2x3xfour").isLeft),
  )

end Day02Test

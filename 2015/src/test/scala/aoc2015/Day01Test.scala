package aoc2015

import aoc2015.Day01.*
import zio.*
import zio.test.*

object Day01Test extends ZIOSpecDefault:

  def spec: Spec[Any, Any] = suite("Day01")(
    test("part1 - official samples"):
      assertTrue(
        part1("(())") == 0,
        part1("()()") == 0,
        part1("(((") == 3,
        part1("(()(()(") == 3,
        part1("))(((((") == 3,
        part1("())") == -1,
        part1("))(") == -1,
        part1(")))") == -3,
        part1(")())())") == -3,
      )
    ,
    test("part1 - empty input"):
      assertTrue(part1("") == 0)
    ,
    test("part1 - single characters"):
      assertTrue(part1("(") == 1, part1(")") == -1)
    ,
    test("part1 - real input"):
      readFile("inputs/Day01.txt").map(input => assertTrue(part1(input) == 138))
    ,
    test("part2 - official samples"):
      assertTrue(part2(")") == 1, part2("()())") == 5)
    ,
    test("part2 - never enters basement"):
      assertTrue(part2("") == -1, part2("(") == -1, part2("(())") == -1, part2("()()") == -1)
    ,
    test("part2 - enters basement later"):
      assertTrue(part2("(()))") == 5, part2(")())())") == 1, part2("(((())))") == -1)
    ,
    test("part2 - real input"):
      readFile("inputs/Day01.txt").map(input => assertTrue(part2(input) == 1771)),
  )

end Day01Test

package aoc2015.day01

import aoc2015.util.AocError
import zio.test.*

object Day01Test extends ZIOSpecDefault:

  def spec: Spec[Any, Any] = suite("Day01")(
    test("parse - moves and whitespace"):
      assertTrue(parse("sample", " (\n)\t(") == Right(Seq(Move.Up, Move.Down, Move.Up)))
    ,
    test("parse - invalid instruction"):
      assertTrue(
        parse("sample", "(x)") ==
          Left(AocError.ParseError("sample", "unexpected character 'x' at position 2"))
      )
    ,
    test("readFile - typed moves"):
      readFile("inputs/Day01.txt").map(moves =>
        assertTrue(moves.size == 7000, moves.take(3) == Seq(Move.Up, Move.Down, Move.Up))
      )
    ,
    test("readFile - missing resource"):
      readFile("inputs/Day01-missing.txt").either.map(result =>
        assertTrue(result.left.exists {
          case AocError.FileReadError("inputs/Day01-missing.txt", _) => true
          case _                                                     => false
        })
      )
    ,
    test("part1 - official samples"):
      assertTrue(
        parse("sample", "(())").map(part1) == Right(0),
        parse("sample", "()()").map(part1) == Right(0),
        parse("sample", "(((").map(part1) == Right(3),
        parse("sample", "(()(()(").map(part1) == Right(3),
        parse("sample", "))(((((").map(part1) == Right(3),
        parse("sample", "())").map(part1) == Right(-1),
        parse("sample", "))(").map(part1) == Right(-1),
        parse("sample", ")))").map(part1) == Right(-3),
        parse("sample", ")())())").map(part1) == Right(-3),
      )
    ,
    test("part1 - empty input"):
      assertTrue(parse("sample", "").map(part1) == Right(0))
    ,
    test("part1 - single characters"):
      assertTrue(
        parse("sample", "(").map(part1) == Right(1),
        parse("sample", ")").map(part1) == Right(-1),
      )
    ,
    test("part1 - real input"):
      readFile("inputs/Day01.txt").map(input => assertTrue(part1(input) == 138))
    ,
    test("part2 - official samples"):
      assertTrue(
        parse("sample", ")").map(part2) == Right(1),
        parse("sample", "()())").map(part2) == Right(5),
      )
    ,
    test("part2 - never enters basement"):
      assertTrue(
        parse("sample", "").map(part2) == Right(-1),
        parse("sample", "(").map(part2) == Right(-1),
        parse("sample", "(())").map(part2) == Right(-1),
        parse("sample", "()()").map(part2) == Right(-1),
      )
    ,
    test("part2 - enters basement later"):
      assertTrue(
        parse("sample", "(()))").map(part2) == Right(5),
        parse("sample", ")())())").map(part2) == Right(1),
        parse("sample", "(((())))").map(part2) == Right(-1),
      )
    ,
    test("part2 - real input"):
      readFile("inputs/Day01.txt").map(input => assertTrue(part2(input) == 1771)),
  )

end Day01Test

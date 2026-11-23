package aoc2015

import aoc2015.Day03.*
import aoc2015.util.AocError
import zio.test.*

object Day03Test extends ZIOSpecDefault:

  def spec: Spec[Any, Any] = suite("Day03")(
    test("parse - typed directions and whitespace"):
      assertTrue(
        parse("sample", " ^ v\n>\t< ") ==
          Right(Seq(Direction.North, Direction.South, Direction.East, Direction.West))
      )
    ,
    test("parse - rejects invalid direction"):
      assertTrue(
        parse("sample", "^x") ==
          Left(AocError.ParseError("sample", "unexpected character 'x' at position 2"))
      )
    ,
    test("readFile - whitespace does not consume a turn"):
      readFile("inputs/Day03Whitespace.txt").map(directions => assertTrue(part2(directions) == 3))
    ,
    test("readFile - invalid instruction fails before solving"):
      readFile("inputs/Day03Invalid.txt").either.map(result => assertTrue(result.isLeft))
    ,
    test("part1 - official samples"):
      assertTrue(
        parse("sample", ">").map(part1) == Right(2),
        parse("sample", "^>v<").map(part1) == Right(4),
        parse("sample", "^v^v^v^v^v").map(part1) == Right(2),
      )
    ,
    test("part1 - empty input"):
      assertTrue(parse("sample", "").map(part1) == Right(1))
    ,
    test("part1 - single moves"):
      assertTrue(
        parse("sample", "^").map(part1) == Right(2),
        parse("sample", "v").map(part1) == Right(2),
        parse("sample", "<").map(part1) == Right(2),
      )
    ,
    test("part1 - back and forth"):
      assertTrue(
        parse("sample", "<>").map(part1) == Right(2),
        parse("sample", "^v").map(part1) == Right(2),
      )
    ,
    test("part1 - square"):
      assertTrue(parse("sample", "^>v<").map(part1) == Right(4))
    ,
    test("part1 - real input"):
      readFile("inputs/Day03.txt").map(input => assertTrue(part1(input) == 2592))
    ,
    test("part2 - official samples"):
      assertTrue(
        parse("sample", "^v").map(part2) == Right(3),
        parse("sample", "^>v<").map(part2) == Right(3),
        parse("sample", "^v^v^v^v^v").map(part2) == Right(11),
      )
    ,
    test("part2 - empty input"):
      assertTrue(parse("sample", "").map(part2) == Right(1))
    ,
    test("part2 - single moves"):
      assertTrue(
        parse("sample", "^").map(part2) == Right(2),
        parse("sample", "v").map(part2) == Right(2),
        parse("sample", ">").map(part2) == Right(2),
        parse("sample", "<").map(part2) == Right(2),
      )
    ,
    test("part2 - directions split across the two movers"):
      assertTrue(
        parse("sample", "<>").map(part2) == Right(3),
        parse("sample", "^v").map(part2) == Right(3),
      )
    ,
    test("part2 - real input"):
      readFile("inputs/Day03.txt").map(input => assertTrue(part2(input) == 2360)),
  )

end Day03Test

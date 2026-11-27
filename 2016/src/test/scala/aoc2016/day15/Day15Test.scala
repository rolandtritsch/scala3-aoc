package aoc2016.day15

import aoc2016.util.AocError
import zio.test.*

object Day15Test extends ZIOSpecDefault:

  private val Example = "Disc #1 has 5 positions; at time=0, it is at position 4.\n" +
    "Disc #2 has 2 positions; at time=0, it is at position 1.\n"

  def spec: Spec[Any, Any] = suite("Day15")(
    test("parse - reads disc number, positions, and start"):
      assertTrue(parse("sample", Example) == Right(Seq(Disc(1, 5, 4), Disc(2, 2, 1))))
    ,
    test("parse - rejects blank, malformed, and out-of-range input"):
      assertTrue(
        Seq(
          "",
          "Disc #1 has 5 positions; at time=0, it is at position 9.",
          "Disc #0 has 5 positions; at time=0, it is at position 0.",
          "Disc #1 has 0 positions; at time=0, it is at position 0.",
          "not a disc",
        ).forall(input => parse("sample", input).isLeft)
      )
    ,
    test("parse - reports the input filename"):
      assertTrue(parse("sample", "not a disc").left.exists {
        case AocError.ParseError("sample", _) => true
        case _                                => false
      })
    ,
    test("part1 - official example"):
      assertTrue(parse("sample", Example).map(part1) == Right(5))
    ,
    test("readFile - personal input"):
      readFile("inputs/Day15.txt").map(discs => assertTrue(discs.size == 6))
    ,
    test("part1 - personal input"):
      readFile("inputs/Day15.txt").map(discs => assertTrue(part1(discs) == 121834))
    ,
    test("part2 - official example gains an 11-position disc"):
      assertTrue(parse("sample", Example).map(part2) == Right(85))
    ,
    test("part2 - personal input"):
      readFile("inputs/Day15.txt").map(discs => assertTrue(part2(discs) == 3208099)),
  )

  end spec

end Day15Test

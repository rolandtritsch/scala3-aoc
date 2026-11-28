package aoc2016.day20

import aoc2016.util.AocError
import zio.test.*

object Day20Test extends ZIOSpecDefault:

  def spec: Spec[Any, Any] = suite("Day20")(
    test("parse - reads inclusive start-end ranges and ignores blank lines"):
      assertTrue(
        parse("sample", "5-8\n0-2\n4-7\n") ==
          Right(Input(Seq(BlockedRange(5, 8), BlockedRange(0, 2), BlockedRange(4, 7)))),
        parse("sample", "\n 0-0 \n\n") == Right(Input(Seq(BlockedRange(0, 0)))),
        parse("sample", "4294967295-4294967295") ==
          Right(Input(Seq(BlockedRange(4294967295L, 4294967295L)))),
      )
    ,
    test("parse - rejects empty, malformed, reversed, negative and overflowing input"):
      assertTrue(
        Seq("", "\n", "5", "5-8-9", "8-5", "-1-3", "0-4294967296", "a-b")
          .forall(input => parse("sample", input).isLeft)
      )
    ,
    test("parse - reports the input filename"):
      assertTrue(parse("sample", "abc").left.exists {
        case AocError.ParseError("sample", _) => true
        case _                                => false
      })
    ,
    test("part1 - official example leaves 3 and 9"):
      assertTrue(
        part1(Input(Seq(BlockedRange(5, 8), BlockedRange(0, 2), BlockedRange(4, 7)))) == 3L
      )
    ,
    test("part1 - lowest IP when zero is blocked"):
      assertTrue(part1(Input(Seq(BlockedRange(0, 5)))) == 6L)
    ,
    test("part1 - everything blocked yields -1"):
      assertTrue(part1(Input(Seq(BlockedRange(0, 4294967295L)))) == -1L)
    ,
    test("readFile - personal input"):
      readFile("inputs/Day20.txt").map(input => assertTrue(input.ranges.size == 1176))
    ,
    test("part1 - personal input"):
      readFile("inputs/Day20.txt").map(input => assertTrue(part1(input) == 4793564L)),
  )

  end spec

end Day20Test

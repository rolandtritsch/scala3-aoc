package aoc2016.day05

import aoc2016.util.AocError
import zio.test.*

object Day05Test extends ZIOSpecDefault:

  def spec: Spec[Any, Any] = suite("Day05")(
    test("parse - accepts a Door ID with LF or CRLF line endings"):
      assertTrue(
        parse("sample", "abc").map(_.value) == Right("abc"),
        parse("sample", "abc\n").map(_.value) == Right("abc"),
        parse("sample", "abc\r\n").map(_.value) == Right("abc"),
      )
    ,
    test("parse - rejects empty, blank, whitespace-containing, and multiple-line input"):
      assertTrue(
        Seq("", " \n", "ab c\n", "abc\nxyz", "abc\n\n")
          .forall(input => parse("sample", input).isLeft)
      )
    ,
    test("parse - reports the input filename"):
      assertTrue(parse("sample", "").left.exists {
        case AocError.ParseError("sample", _) => true
        case _                                => false
      })
    ,
    test("part1 - official example"):
      assertTrue(parse("sample", "abc").map(part1) == Right("18f47a30"))
    ,
    test("part2 - official example"):
      assertTrue(parse("sample", "abc").map(part2) == Right("05ace8e3"))
    ,
    test("readFile - personal input"):
      readFile("inputs/Day05.txt").map(doorId => assertTrue(doorId.value == "ffykfhsq"))
    ,
    test("part1 - personal input"):
      readFile("inputs/Day05.txt").map(doorId => assertTrue(part1(doorId) == "c6697b55"))
    ,
    test("part2 - personal input"):
      readFile("inputs/Day05.txt").map(doorId => assertTrue(part2(doorId) == "8c35d1ab")),
  )

  end spec

end Day05Test

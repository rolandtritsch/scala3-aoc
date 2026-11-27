package aoc2016.day14

import aoc2016.util.AocError
import zio.test.*

object Day14Test extends ZIOSpecDefault:

  def spec: Spec[Any, Any] = suite("Day14")(
    test("parse - accepts a salt with LF or CRLF line endings"):
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
    test("firstTriplet - finds only the first run of three"):
      assertTrue(
        firstTriplet("abc") == None,
        firstTriplet("a777z") == Some('7'),
        firstTriplet("77a777") == Some('7'),
      )
    ,
    test("hasQuint - detects five in a row of the given character"):
      assertTrue(
        hasQuint("abc", 'a') == false,
        hasQuint("x77777y", '7') == true,
        hasQuint("x7777y", '7') == false,
      )
    ,
    test("part1 - official example"):
      assertTrue(parse("sample", "abc").map(part1) == Right(22728))
    ,
    test("readFile - personal input"):
      readFile("inputs/Day14.txt").map(salt => assertTrue(salt.value == "qzyelonm"))
    ,
    test("part1 - personal input"):
      readFile("inputs/Day14.txt").map(salt => assertTrue(part1(salt) == 15168)),
  )

  end spec

end Day14Test

package aoc2016.day07

import aoc2016.util.AocError
import zio.test.*

object Day07Test extends ZIOSpecDefault:

  private val officialExample = """abba[mnop]qrst
                                  |abcd[bddb]xyyx
                                  |aaaa[qwer]tyui
                                  |ioxxoj[asdfgh]zxcvbn""".stripMargin

  def spec: Spec[Any, Any] = suite("Day07")(
    test("parse - accepts LF and CRLF input with a final newline"):
      assertTrue(
        parse("sample", "abba[mnop]qrst\n").map(part1) == Right(1),
        parse("sample", "abba[mnop]qrst\r\n").map(part1) == Right(1),
      )
    ,
    test("parse - rejects empty, blank, and malformed addresses"):
      assertTrue(
        Seq("", "\n", "abba\n\n", "ab ba", "abba[", "abba[]qrst", "ABBA[mnop]qrst")
          .forall(input => parse("sample", input).isLeft)
      )
    ,
    test("parse - reports the filename and malformed line"):
      assertTrue(parse("sample", "abba\nabcd[").left.exists {
        case AocError.ParseError("sample", message) => message.contains("line 2")
        case _                                      => false
      })
    ,
    test("part1 - official examples"):
      assertTrue(parse("sample", officialExample).map(part1) == Right(2))
    ,
    test("part1 - hypernet ABBA vetoes support and brackets break windows"):
      assertTrue(
        parse("sample", "abba[xyyx]qrst").map(part1) == Right(0),
        parse("sample", "ab[ba]ab").map(part1) == Right(0),
      )
    ,
    test("part1 - personal input"):
      readFile("inputs/Day07.txt").map(addresses => assertTrue(part1(addresses) == 105)),
  )

  end spec

end Day07Test

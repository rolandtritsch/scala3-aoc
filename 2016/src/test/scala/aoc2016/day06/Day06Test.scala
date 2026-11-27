package aoc2016.day06

import aoc2016.util.AocError
import zio.test.*

object Day06Test extends ZIOSpecDefault:

  private val officialExample = """eedadn
                                  |drvtee
                                  |eandsr
                                  |raavrd
                                  |atevrs
                                  |tsrnev
                                  |sdttsa
                                  |rasrtv
                                  |nssdts
                                  |ntnada
                                  |svetve
                                  |tesnvt
                                  |vntsnd
                                  |vrdear
                                  |dvrsen
                                  |enarar""".stripMargin

  def spec: Spec[Any, Any] = suite("Day06")(
    test("parse - accepts LF and CRLF input with a final newline"):
      assertTrue(
        parse("sample", "ab\nab\n").map(part1) == Right("ab"),
        parse("sample", "ab\r\nab\r\n").map(part1) == Right("ab"),
      )
    ,
    test("parse - rejects empty, malformed, blank, and unequal-width signals"):
      assertTrue(
        Seq("", "\n", "ab\nab\n\n", "ab\nc", "ab\ncde", "ab\nc!", "Ab\ncd", "ab\nc d")
          .forall(input => parse("sample", input).isLeft)
      )
    ,
    test("parse - reports the filename and malformed line"):
      assertTrue(parse("sample", "abc\nde").left.exists {
        case AocError.ParseError("sample", message) => message.contains("line 2")
        case _                                      => false
      })
    ,
    test("part1 - official example"):
      assertTrue(parse("sample", officialExample).map(part1) == Right("easter"))
    ,
    test("part1 - personal input"):
      readFile("inputs/Day06.txt").map(recording => assertTrue(part1(recording) == "ursvoerv")),
  )

  end spec

end Day06Test

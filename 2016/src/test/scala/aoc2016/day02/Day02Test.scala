package aoc2016.day02

import aoc2016.util.AocError
import zio.test.*

object Day02Test extends ZIOSpecDefault:

  def spec: Spec[Any, Any] = suite("Day02")(
    test("parse - converts each line into directions"):
      assertTrue(
        parse("sample", "UD\nLR") ==
          Right(Seq(Seq(Direction.Up, Direction.Down), Seq(Direction.Left, Direction.Right)))
      )
    ,
    test("parse - permits surrounding whitespace and CRLF line endings"):
      assertTrue(
        parse("sample", " \tUD \r\n LR\t\n") ==
          Right(Seq(Seq(Direction.Up, Direction.Down), Seq(Direction.Left, Direction.Right)))
      )
    ,
    test("parse - rejects empty input, blank instruction lines and invalid directions"):
      assertTrue(
        Seq("", "  \n", "U\n\nD", "U\n \nD", "U X", "U\nR?", "U\n\n")
          .forall(input => parse("sample", input).isLeft)
      )
    ,
    test("parse - reports the line and character position"):
      assertTrue(parse("sample", "U\nLX").left.exists {
        case AocError.ParseError("sample", message) => message.contains("line 2") &&
          message.contains("character 2")
        case _ => false
      })
    ,
    test("part1 - official example"):
      assertTrue(parse("sample", "ULL\nRRDDD\nLURDL\nUUUUUD").map(part1) == Right("1985"))
    ,
    test("part2 - official example"):
      assertTrue(parse("sample", "ULL\nRRDDD\nLURDL\nUUUUUD").map(part2) == Right("5DB3"))
    ,
    test("readFile - personal input"):
      readFile("inputs/Day02.txt").map(input => assertTrue(input.nonEmpty))
    ,
    test("part1 - personal input"):
      readFile("inputs/Day02.txt").map(input => assertTrue(part1(input) == "65556"))
    ,
    test("part2 - personal input"):
      readFile("inputs/Day02.txt").map(input => assertTrue(part2(input) == "CB779")),
  )

  end spec

end Day02Test

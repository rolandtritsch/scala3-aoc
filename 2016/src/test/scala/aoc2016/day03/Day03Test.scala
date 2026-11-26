package aoc2016.day03

import aoc2016.util.AocError
import zio.test.*

object Day03Test extends ZIOSpecDefault:

  def spec: Spec[Any, Any] = suite("Day03")(
    test("parse - converts each row into a triangle"):
      assertTrue(
        parse("sample", "5 10 25\n3 4 5") == Right(Seq(Triangle(5, 10, 25), Triangle(3, 4, 5)))
      )
    ,
    test("parse - permits separator and surrounding whitespace and CRLF line endings"):
      assertTrue(
        parse("sample", " \t5  10\t25 \r\n 3 4 5\t\n") ==
          Right(Seq(Triangle(5, 10, 25), Triangle(3, 4, 5)))
      )
    ,
    test("parse - rejects empty, blank, malformed, nonpositive and overflowing input"):
      assertTrue(
        Seq(
          "",
          "  \n",
          "5 10",
          "5 10 25 30",
          "5 10 25\n\n3 4 5",
          "5 0 25",
          "5 -10 25",
          "5 nope 25",
          "5 2147483648 25",
        ).forall(input => parse("sample", input).isLeft)
      )
    ,
    test("parse - reports the line and side position"):
      assertTrue(parse("sample", "3 4 5\n6 nope 8").left.exists {
        case AocError.ParseError("sample", message) => message.contains("line 2") &&
          message.contains("side 2")
        case _ => false
      })
    ,
    test("part1 - official example"):
      assertTrue(part1(Seq(Triangle(5, 10, 25))) == 0)
    ,
    test("part1 - requires every pairwise sum to exceed the remaining side"):
      assertTrue(
        part1(Seq(Triangle(3, 4, 5), Triangle(2, 2, 3), Triangle(2, 2, 4), Triangle(1, 2, 3))) == 2
      )
    ,
    test("part2 - official column example"):
      val input = Seq(
        Triangle(101, 301, 501),
        Triangle(102, 302, 502),
        Triangle(103, 303, 503),
        Triangle(201, 401, 601),
        Triangle(202, 402, 602),
        Triangle(203, 403, 603),
      )
      assertTrue(part2(input) == Right(6))
    ,
    test("part2 - reads columns instead of rows"):
      val input = Seq(Triangle(5, 10, 25), Triangle(3, 4, 5), Triangle(2, 2, 3))
      assertTrue(part1(input) == 2 && part2(input) == Right(0))
    ,
    test("part2 - rejects an incomplete final column group"):
      assertTrue(part2(Seq(Triangle(3, 4, 5))).isLeft)
    ,
    test("readFile - personal input"):
      readFile("inputs/Day03.txt").map(input => assertTrue(input.nonEmpty))
    ,
    test("part1 - personal input"):
      readFile("inputs/Day03.txt").map(input => assertTrue(part1(input) == 869))
    ,
    test("part2 - personal input"):
      readFile("inputs/Day03.txt").map(input => assertTrue(part2(input) == Right(1544))),
  )

  end spec

end Day03Test

package aoc2015.day02

import scala.compiletime.testing.typeCheckErrors

import aoc2015.day02.Present.parseLine
import aoc2015.util.AocError
import zio.test.*

object Day02Test extends ZIOSpecDefault:

  def spec: Spec[Any, Any] = suite("Day02")(
    test("domain values cannot bypass validation through copy"):
      assertTrue(
        typeCheckErrors("""aoc2015.day02.Present.parseLine("sample", "2x3x4").map(_.length)""")
          .isEmpty,
        typeCheckErrors(
          """aoc2015.day02.Present.parseLine("sample", "2x3x4").map(_.copy(length = BigInt(0)))"""
        ).nonEmpty,
      )
    ,
    test("parse - rejects invalid dimensions"):
      assertTrue(
        Seq("0x2x3", "-1x2x3", "2x3x4x", "2x3x4x5", "2xx3", "", "2x3x4.0")
          .forall(line => parseLine("sample", line).isLeft)
      )
    ,
    test("parse - surrounding whitespace and exact large volumes"):
      assertTrue(
        parseLine("sample", " 2x3x4 ").map(ribbon) == Right(BigInt(34)),
        parseLine("sample", "2147483648x2147483648x2147483648").map(ribbon) ==
          Right(BigInt("9903520314283042207782928384")),
      )
    ,
    test("readFile - invalid line includes its location"):
      readFile("inputs/Day02Invalid.txt").either.map(result =>
        assertTrue(result.left.exists {
          case AocError.ParseError("inputs/Day02Invalid.txt", message) => message.contains("line 3")
          case _                                                       => false
        })
      )
    ,
    test("parts - empty sequence"):
      assertTrue(part1(Seq.empty) == 0, part2(Seq.empty) == 0)
    ,
    test("part1 - official samples"):
      assertTrue(
        parseLine("sample", "2x3x4").map(wrappingPaper) == Right(BigInt(58)),
        parseLine("sample", "1x1x10").map(wrappingPaper) == Right(BigInt(43)),
      )
    ,
    test("part1 - cube"):
      assertTrue(parseLine("sample", "1x1x1").map(wrappingPaper) == Right(BigInt(7)))
    ,
    test("part1 - two equal dimensions"):
      assertTrue(parseLine("sample", "2x2x3").map(wrappingPaper) == Right(BigInt(36)))
    ,
    test("part1 - readFile"):
      readFile("inputs/Day02.txt").map(obtained =>
        assertTrue(
          obtained.length == 1000,
          obtained.headOption.exists(p => p.length == 20 && p.width == 3 && p.height == 11),
        )
      )
    ,
    test("part1 - real input"):
      readFile("inputs/Day02.txt").map(input => assertTrue(part1(input) == 1606483))
    ,
    test("part2 - official samples"):
      assertTrue(
        parseLine("sample", "2x3x4").map(ribbon) == Right(BigInt(34)),
        parseLine("sample", "1x1x10").map(ribbon) == Right(BigInt(14)),
      )
    ,
    test("part2 - cube"):
      assertTrue(parseLine("sample", "1x1x1").map(ribbon) == Right(BigInt(5)))
    ,
    test("part2 - two equal dimensions"):
      assertTrue(parseLine("sample", "2x2x3").map(ribbon) == Right(BigInt(20)))
    ,
    test("part2 - real input"):
      readFile("inputs/Day02.txt").map(input => assertTrue(part2(input) == 3842356))
    ,
    test("parseLine - rejects malformed line"):
      assertTrue(parseLine("test", "2x3").isLeft, parseLine("test", "2x3xfour").isLeft),
  )

end Day02Test

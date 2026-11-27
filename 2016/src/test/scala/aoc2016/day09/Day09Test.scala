package aoc2016.day09

import zio.test.*

object Day09Test extends ZIOSpecDefault:

  private val officialExamples = Seq(
    "ADVENT" -> BigInt(6),
    "A(1x5)BC" -> BigInt(7),
    "(3x3)XYZ" -> BigInt(9),
    "A(2x2)BCD(2x2)EFG" -> BigInt(11),
    "(6x1)(1x3)A" -> BigInt(6),
    "X(8x2)(3x3)ABCY" -> BigInt(18),
  )

  def spec: Spec[Any, Any] = suite("Day09")(
    test("parse - ignores whitespace and captures repeated data verbatim"):
      assertTrue(
        parse("sample", " X(8x2)(3x3)A B C Y \n") == Right(
          CompressedFile(Seq(Chunk.Literal("X"), Chunk.Repeat("(3x3)ABC", 2), Chunk.Literal("Y")))
        )
      )
    ,
    test("parse - rejects malformed, truncated and overflowing markers"):
      assertTrue(
        Seq("A(", "A(2x2", "A(2y2)BC", "A(3x1)BC", "(2147483648x1)A", "(1x2147483648)A")
          .forall(input => parse("sample", input).isLeft)
      )
    ,
    test("part1 - official examples"):
      assertTrue(officialExamples.forall { case (input, expected) =>
        parse("sample", input).map(part1) == Right(expected)
      })
    ,
    test("readFile and part1 - personal input"):
      readFile("inputs/Day09.txt").map(input => assertTrue(part1(input) == BigInt(97714))),
  )
  end spec

end Day09Test

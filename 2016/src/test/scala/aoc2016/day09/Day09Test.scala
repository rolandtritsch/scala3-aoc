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

  private val officialPart2Examples = Seq(
    "(3x3)XYZ" -> BigInt(9),
    "X(8x2)(3x3)ABCY" -> BigInt(20),
    "(27x12)(20x12)(13x14)(7x10)(1x12)A" -> BigInt(241920),
    "(25x3)(3x3)ABC(2x3)XY(5x2)PQRSTX(18x9)(3x2)TWO(5x7)SEVEN" -> BigInt(445),
  )

  def spec: Spec[Any, Any] = suite("Day09")(
    test("parse - ignores whitespace and captures repeated data verbatim"):
      assertTrue(
        parse("sample", " X(8x2)(3x3)A B C Y \n") == Right(CompressedFile(Seq(
          Chunk.Literal("X"),
          Chunk.Repeat(
            "(3x3)ABC",
            2,
            CompressedFile(Seq(Chunk.Repeat("ABC", 3, CompressedFile(Seq(Chunk.Literal("ABC")))))),
          ),
          Chunk.Literal("Y"),
        )))
      )
    ,
    test("parse - rejects malformed, truncated and overflowing markers"):
      assertTrue(
        Seq("A(", "A(2x2", "A(2y2)BC", "A(3x1)BC", "(2x1)A(", "(2147483648x1)A", "(1x2147483648)A")
          .forall(input => parse("sample", input).isLeft)
      )
    ,
    test("part1 - official examples"):
      assertTrue(officialExamples.forall { case (input, expected) =>
        parse("sample", input).map(part1) == Right(expected)
      })
    ,
    test("part2 - official examples"):
      assertTrue(officialPart2Examples.forall { case (input, expected) =>
        parse("sample", input).map(part2) == Right(expected)
      })
    ,
    test("readFile and part1 - personal input"):
      readFile("inputs/Day09.txt").map(input => assertTrue(part1(input) == BigInt(97714)))
    ,
    test("readFile and part2 - personal input"):
      readFile("inputs/Day09.txt").map(input => assertTrue(part2(input) == BigInt(10762972461L))),
  )
  end spec

end Day09Test

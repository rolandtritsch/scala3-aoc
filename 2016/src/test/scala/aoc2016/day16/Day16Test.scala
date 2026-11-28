package aoc2016.day16

import aoc2016.util.AocError
import zio.test.*

object Day16Test extends ZIOSpecDefault:

  def spec: Spec[Any, Any] = suite("Day16")(
    test("parse - reads a single row of bits and ignores blank lines"):
      assertTrue(
        parse("sample", "10000\n") ==
          Right(Input(Seq(Bit.One, Bit.Zero, Bit.Zero, Bit.Zero, Bit.Zero))),
        parse("sample", "\n 0110 \n\n") == Right(Input(Seq(Bit.Zero, Bit.One, Bit.One, Bit.Zero))),
      )
    ,
    test("parse - rejects empty, multi-row, and non-binary input"):
      assertTrue(
        Seq("", "\n", "10\n01", "102", "abc").forall(input => parse("sample", input).isLeft)
      )
    ,
    test("parse - reports the input filename"):
      assertTrue(parse("sample", "abc").left.exists {
        case AocError.ParseError("sample", _) => true
        case _                                => false
      })
    ,
    test("part1 - official example"):
      assertTrue(
        parse("sample", "10000").map(input => checksum(dragon(input.bits, 20).take(20))) ==
          Right("01100")
      )
    ,
    test("part2 - official example"):
      assertTrue(
        parse("sample", "10000").map(input => checksumFor(input.bits, 20)) == Right("01100")
      )
    ,
    test("readFile - personal input"):
      readFile("inputs/Day16.txt").map(input => assertTrue(input.bits.size == 17))
    ,
    test("part1 - personal input"):
      readFile("inputs/Day16.txt").map(input => assertTrue(part1(input) == "10100011010101011"))
    ,
    test("part2 - personal input"):
      readFile("inputs/Day16.txt").map(input => assertTrue(part2(input) == "01010001101011001")),
  )

  end spec

end Day16Test

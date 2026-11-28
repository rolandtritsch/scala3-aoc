package aoc2016.day19

import aoc2016.util.AocError
import zio.test.*

object Day19Test extends ZIOSpecDefault:

  def spec: Spec[Any, Any] = suite("Day19")(
    test("parse - reads a single positive Elf count and ignores blank lines"):
      assertTrue(
        parse("sample", "5\n") == Right(Input(5)),
        parse("sample", "\n 3014603 \n\n") == Right(Input(3014603)),
      )
    ,
    test("parse - rejects empty, multiple, non-positive, malformed and overflowing input"):
      assertTrue(
        Seq("", "\n", "5\n6", "0", "-3", "abc", "2147483648")
          .forall(input => parse("sample", input).isLeft)
      )
    ,
    test("parse - reports the input filename"):
      assertTrue(parse("sample", "abc").left.exists {
        case AocError.ParseError("sample", _) => true
        case _                                => false
      })
    ,
    test("part1 - official five-Elf example leaves Elf 3"):
      assertTrue(part1(Input(5)) == 3)
    ,
    test("part1 - small circles"):
      assertTrue(
        part1(Input(1)) == 1,
        part1(Input(2)) == 1,
        part1(Input(3)) == 3,
        part1(Input(4)) == 1,
        part1(Input(6)) == 5,
        part1(Input(7)) == 7,
      )
    ,
    test("part2 - official five-Elf example leaves Elf 2"):
      assertTrue(part2(Input(5)) == 2)
    ,
    test("part2 - small circles"):
      assertTrue(
        part2(Input(1)) == 1,
        part2(Input(2)) == 1,
        part2(Input(3)) == 3,
        part2(Input(4)) == 1,
        part2(Input(6)) == 3,
        part2(Input(7)) == 5,
        part2(Input(9)) == 9,
      )
    ,
    test("readFile - personal input"):
      readFile("inputs/Day19.txt").map(input => assertTrue(input.elfCount == 3014603))
    ,
    test("part1 - personal input"):
      readFile("inputs/Day19.txt").map(input => assertTrue(part1(input) == 1834903))
    ,
    test("part2 - personal input"):
      readFile("inputs/Day19.txt").map(input => assertTrue(part2(input) == 1420280)),
  )

  end spec

end Day19Test

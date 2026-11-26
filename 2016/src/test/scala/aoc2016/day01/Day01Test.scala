package aoc2016.day01

import aoc2016.util.AocError
import zio.test.*

object Day01Test extends ZIOSpecDefault:

  def spec: Spec[Any, Any] = suite("Day01")(
    test("parse - converts turns and distances into instructions"):
      assertTrue(
        parse("sample", "R2, L3") ==
          Right(Seq(Instruction(Turn.Right, 2), Instruction(Turn.Left, 3)))
      )
    ,
    test("parse - permits surrounding and separator whitespace"):
      assertTrue(
        parse("sample", " \n R2 ,\n L3 \t") ==
          Right(Seq(Instruction(Turn.Right, 2), Instruction(Turn.Left, 3)))
      )
    ,
    test("parse - rejects empty, malformed, nonpositive and overflowing instructions"):
      assertTrue(
        Seq("", "   \n", "R2,", ",R2", "R2,,L3", "R", "R0", "R-2", "X2", "R2 L3", "R2147483648")
          .forall(input => parse("sample", input).isLeft)
      )
    ,
    test("parse - reports the invalid instruction position"):
      assertTrue(parse("sample", "R2, nope").left.exists {
        case AocError.ParseError("sample", message) => message.contains("instruction 2")
        case _                                      => false
      })
    ,
    test("part1 - official examples"):
      assertTrue(Seq("R2, L3" -> 5, "R2, R2, R2" -> 2, "R5, L5, R5, R3" -> 12).forall {
        case (input, answer) => parse("sample", input).map(part1) == Right(answer)
      })
    ,
    test("part1 - left turn from initial north"):
      assertTrue(parse("sample", "L2").map(part1) == Right(2))
    ,
    test("part2 - official example"):
      assertTrue(parse("sample", "R8, R4, R4, R8").map(part2) == Right(Some(4)))
    ,
    test("part2 - detects a return to the starting location"):
      assertTrue(parse("sample", "R1, R1, R1, R1").map(part2) == Right(Some(0)))
    ,
    test("part2 - returns no answer when no location is revisited"):
      assertTrue(parse("sample", "R1, L1").map(part2) == Right(None))
    ,
    test("readFile - personal input"):
      readFile("inputs/Day01.txt").map(input => assertTrue(input.nonEmpty))
    ,
    test("part1 - personal input"):
      readFile("inputs/Day01.txt").map(input => assertTrue(part1(input) == 241))
    ,
    test("part2 - personal input"):
      readFile("inputs/Day01.txt").map(input => assertTrue(part2(input) == Some(116))),
  )

  end spec

end Day01Test

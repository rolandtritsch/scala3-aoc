package aoc2016.day08

import aoc2016.util.AocError
import zio.test.*

object Day08Test extends ZIOSpecDefault:

  private val officialExample = """rect 3x2
                                  |rotate column x=1 by 1
                                  |rotate row y=0 by 4
                                  |rotate column x=1 by 1""".stripMargin

  private val officialFinalPixels =
    Set(Pixel(1, 0), Pixel(4, 0), Pixel(6, 0), Pixel(0, 1), Pixel(2, 1), Pixel(1, 2))

  def spec: Spec[Any, Any] = suite("Day08")(
    test("parse - accepts LF and CRLF input and maps instructions to domain values"):
      val expected = Right(Seq(Instruction.Rect(3, 2), Instruction.RotateRow(0, 4)))
      assertTrue(
        parse("sample", "rect 3x2\nrotate row y=0 by 4\n") == expected,
        parse("sample", "rect 3x2\r\nrotate row y=0 by 4\r\n") == expected,
      )
    ,
    test("parse - rejects empty, blank, malformed, and out-of-bounds instructions"):
      assertTrue(
        Seq(
          "",
          "\n",
          "rect 1x1\n\n",
          "rect 0x1",
          "rect 51x1",
          "rect 1x7",
          "rotate row y=6 by 1",
          "rotate column x=50 by 1",
          "rotate row y=0 by 2147483648",
          "rect 3X2",
          "rotate row y=0 by -1",
          "unknown instruction",
        ).forall(input => parse("sample", input).isLeft)
      )
    ,
    test("parse - reports the filename and malformed line"):
      assertTrue(parse("sample", "rect 1x1\nrotate row y=six by 1").left.exists {
        case AocError.ParseError("sample", message) => message.contains("line 2")
        case _                                      => false
      })
    ,
    test("part1 - official example produces the documented final display"):
      assertTrue(parse("sample", officialExample).map { instructions =>
        val screen = runScreen(7, 3, instructions)
        screen.lit == officialFinalPixels && screen.lit.size == 6
      } == Right(true))
    ,
    test("part1 - row and column rotations wrap after complete turns"):
      assertTrue(
        runScreen(7, 3, Seq(Instruction.Rect(1, 1), Instruction.RotateRow(0, 8))).lit ==
          Set(Pixel(1, 0)),
        runScreen(7, 3, Seq(Instruction.Rect(1, 1), Instruction.RotateColumn(0, 4))).lit ==
          Set(Pixel(0, 1)),
      )
    ,
    test("part1 - overlapping rectangles do not double-count pixels"):
      assertTrue(part1(Seq(Instruction.Rect(2, 2), Instruction.Rect(2, 2))) == 4)
    ,
    test("part1 - personal input"):
      readFile("inputs/Day08.txt").map(instructions => assertTrue(part1(instructions) == 106)),
  )

  end spec

end Day08Test

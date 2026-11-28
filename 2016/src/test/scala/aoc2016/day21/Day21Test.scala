package aoc2016.day21

import aoc2016.util.AocError
import zio.test.*

object Day21Test extends ZIOSpecDefault:

  private val example: String = """swap position 4 with position 0
                                  |swap letter d with letter b
                                  |reverse positions 0 through 4
                                  |rotate left 1 step
                                  |move position 1 to position 4
                                  |move position 3 to position 0
                                  |rotate based on position of letter b
                                  |rotate based on position of letter d
                                  |""".stripMargin

  def spec: Spec[Any, Any] = suite("Day21")(
    test("parse - reads every instruction kind and ignores blank lines"):
      assertTrue(
        parse("sample", example) == Right(Input(Seq(
          Operation.SwapPosition(4, 0),
          Operation.SwapLetter('d', 'b'),
          Operation.Reverse(0, 4),
          Operation.Rotate(1, Rotation.Left),
          Operation.Move(1, 4),
          Operation.Move(3, 0),
          Operation.RotateLetter('b'),
          Operation.RotateLetter('d'),
        ))),
        parse("sample", "rotate right 3 steps\n\n") ==
          Right(Input(Seq(Operation.Rotate(3, Rotation.Right)))),
      )
    ,
    test("parse - rejects empty and malformed input"):
      assertTrue(
        Seq("", "\n", "swap position 4", "rotate up 2 steps", "reverse positions 0", "frobnicate")
          .forall(input => parse("sample", input).isLeft)
      )
    ,
    test("parse - reports the input filename"):
      assertTrue(parse("sample", "nonsense").left.exists {
        case AocError.ParseError("sample", _) => true
        case _                                => false
      })
    ,
    test("part1 - official example produces decab"):
      val input = parse("sample", example).toOption.get
      assertTrue(scramble("abcde", input.operations) == "decab")
    ,
    test("part1 - personal input"):
      readFile("inputs/Day21.txt").map(input => assertTrue(part1(input) == "bfheacgd")),
  )

  end spec

end Day21Test

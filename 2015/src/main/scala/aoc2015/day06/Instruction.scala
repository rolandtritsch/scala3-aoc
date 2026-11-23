package aoc2015.day06

import aoc2015.util.AocError
import aoc2015.util.Position

final case class Instruction private (action: Action, from: Position, to: Position)

object Instruction:
  private val instructionPattern = """(turn on|turn off|toggle) (\d+),(\d+) through (\d+),(\d+)""".r

  /** Validate numeric conversion, grid bounds, and corner ordering before constructing an
    * instruction.
    */
  def parse(filename: String, line: String, lineNumber: Int = 1): Either[AocError, Instruction] =
    def invalid(reason: String): Left[AocError, Nothing] =
      Left(AocError.ParseError(filename, s"line $lineNumber: $reason: $line"))

    line.trim match
      case instructionPattern(operation, rawX1, rawY1, rawX2, rawY2) =>
        val action = operation match
          case "turn on"  => Right(Action.TurnOn)
          case "turn off" => Right(Action.TurnOff)
          case "toggle"   => Right(Action.Toggle)
          case _          => invalid("unrecognized operation")
        val coordinates =
          (rawX1.toIntOption, rawY1.toIntOption, rawX2.toIntOption, rawY2.toIntOption) match
            case (Some(x1), Some(y1), Some(x2), Some(y2))
              if x1 >= 0 && y1 >= 0 && x2 < gridSide && y2 < gridSide && x1 <= x2 && y1 <= y2 =>
              Right((Position(x1, y1), Position(x2, y2)))
            case _ => invalid("expected ordered corners within 0..999")
        for
          parsedAction <- action
          (from, to) <- coordinates
        yield Instruction(parsedAction, from, to)
      case _ => invalid("unrecognized instruction")
    end match

  end parse

end Instruction

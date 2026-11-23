package aoc2015

import aoc2015.util.AocError
import zio.*

/** Day06 - Probably a Fire Hazard. */
object Day06:

  private val gridSide: Int = 1000

  enum Action:
    case TurnOn, TurnOff, Toggle

  final case class Position(x: Int, y: Int)
  final case class Instruction private[Day06] (action: Action, from: Position, to: Position)

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

  /** Blank lines are ignored; both parts receive the same validated instructions. */
  def readFile(filename: String): ZIO[Any, AocError, Seq[Instruction]] =
    import scala.io.Source

    ZIO.acquireReleaseWith(
      ZIO.attempt(Source.fromResource(filename)).mapError(AocError.FileReadError(filename, _))
    )(source => ZIO.succeed(source.close())) { source =>
      for
        _ <- ZIO.logDebug(s"filename: $filename")
        lines <- ZIO.attempt(source.getLines().toSeq).mapError(AocError.FileReadError(filename, _))
        instructions <- ZIO.foreach(lines.zipWithIndex.filter(_._1.trim.nonEmpty)) { (line, index) =>
          for
            _ <- ZIO.logDebug(s"line ${index + 1}: $line")
            instruction <- ZIO.fromEither(parse(filename, line, index + 1))
            _ <- ZIO.logDebug(s"parsed: $instruction")
          yield instruction
        }
      yield instructions
    }

  end readFile

  private def switched(on: Boolean, action: Action): Boolean = action match
    case Action.TurnOn  => true
    case Action.TurnOff => false
    case Action.Toggle  => !on

  private def brightness(current: Long, action: Action): Long = action match
    case Action.TurnOn  => current + 1
    case Action.TurnOff => (current - 1).max(0L)
    case Action.Toggle  => current + 2

  /** Arrays provide constant-time cell updates without copying the million-cell grid. Mutation
    * stays local, so evaluating either part cannot change its input or another run.
    */
  def part1(instructions: Seq[Instruction]): Int =
    val lights = Array.fill[Boolean](gridSide * gridSide)(false)
    for
      instruction <- instructions
      y <- instruction.from.y to instruction.to.y
      x <- instruction.from.x to instruction.to.x
    do
      val index = y * gridSide + x // Parsed corners guarantee a valid array index.
      lights(index) = switched(lights(index), instruction.action)
    end for
    lights.count(identity)

  end part1

  /** Long brightness and totals avoid Int overflow across large instruction sequences. A local
    * array provides constant-time cell updates, as in part1.
    */
  def part2(instructions: Seq[Instruction]): Long =
    val lights = Array.fill[Long](gridSide * gridSide)(0L)
    for
      instruction <- instructions
      y <- instruction.from.y to instruction.to.y
      x <- instruction.from.x to instruction.to.x
    do
      val index = y * gridSide + x // Parsed corners guarantee a valid array index.
      lights(index) = brightness(lights(index), instruction.action)
    end for
    lights.sum

  end part2

end Day06

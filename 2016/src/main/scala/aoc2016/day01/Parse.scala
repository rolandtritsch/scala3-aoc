package aoc2016.day01

import aoc2016.util.AocError

private val InstructionPattern = """([LR])([0-9]+)""".r

def parse(filename: String, input: String): Either[AocError, Seq[Instruction]] =
  if input.trim.isEmpty then
    Left(AocError.ParseError(filename, "expected at least one instruction"))
  else
    input.split(",", -1).toSeq.zipWithIndex
      .foldLeft[Either[AocError, Seq[Instruction]]](Right(Seq.empty)) {
        case (result, (token, index)) =>
          for
            instructions <- result
            instruction <- parseInstruction(filename, token, index + 1)
          yield instruction +: instructions
      }.map(_.reverse)
end parse

private def parseInstruction(
    filename: String,
    token: String,
    position: Int,
): Either[AocError, Instruction] = token.trim match
  case InstructionPattern(turn, blocks) =>
    val direction = turn match
      case "L" => Turn.Left
      case "R" => Turn.Right
    blocks.toIntOption.filter(_ > 0) match
      case Some(value) => Right(Instruction(direction, value))
      case None        => Left(instructionError(filename, position))
  case _ => Left(instructionError(filename, position))
end parseInstruction

private def instructionError(filename: String, position: Int): AocError.ParseError = AocError
  .ParseError(
    filename,
    s"instruction $position: expected L or R followed by a positive whole number of blocks",
  )

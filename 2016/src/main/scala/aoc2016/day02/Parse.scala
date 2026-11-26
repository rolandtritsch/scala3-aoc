package aoc2016.day02

import aoc2016.util.AocError

def parse(filename: String, input: String): Either[AocError, Seq[Seq[Direction]]] =
  val lines = input.split("\\R", -1).toSeq
  val instructionLines = if lines.lastOption.contains("") then lines.dropRight(1) else lines
  if instructionLines.isEmpty then
    Left(AocError.ParseError(filename, "expected at least one instruction line"))
  else
    instructionLines.zipWithIndex
      .foldLeft[Either[AocError, Seq[Seq[Direction]]]](Right(Seq.empty)) {
        case (result, (line, index)) =>
          for
            instructions <- result
            lineInstructions <- parseLine(filename, line, index + 1)
          yield lineInstructions +: instructions
      }.map(_.reverse)
end parse

private def parseLine(
    filename: String,
    line: String,
    lineNumber: Int,
): Either[AocError, Seq[Direction]] =
  val instructions = line.trim
  if instructions.isEmpty then
    Left(AocError.ParseError(filename, s"line $lineNumber: expected at least one direction"))
  else
    instructions.zipWithIndex.foldLeft[Either[AocError, Seq[Direction]]](Right(Seq.empty)) {
      case (result, (symbol, index)) =>
        for
          directions <- result
          direction <- parseDirection(filename, symbol, lineNumber, index + 1)
        yield direction +: directions
    }.map(_.reverse)
end parseLine

private def parseDirection(
    filename: String,
    symbol: Char,
    lineNumber: Int,
    character: Int,
): Either[AocError, Direction] = symbol match
  case 'U' => Right(Direction.Up)
  case 'D' => Right(Direction.Down)
  case 'L' => Right(Direction.Left)
  case 'R' => Right(Direction.Right)
  case _   => Left(AocError.ParseError(
      filename,
      s"line $lineNumber, character $character: expected U, D, L, or R; found '$symbol'",
    ))
end parseDirection

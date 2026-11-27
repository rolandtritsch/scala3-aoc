package aoc2016.day08

import aoc2016.util.AocError

enum Instruction:
  case Rect(width: Int, height: Int)
  case RotateRow(row: Int, amount: Int)
  case RotateColumn(column: Int, amount: Int)
end Instruction

private[day08] val ScreenWidth = 50
private[day08] val ScreenHeight = 6

private val RectPattern = """rect ([0-9]+)x([0-9]+)""".r
private val RotateRowPattern = """rotate row y=([0-9]+) by ([0-9]+)""".r
private val RotateColumnPattern = """rotate column x=([0-9]+) by ([0-9]+)""".r

def parse(filename: String, input: String): Either[AocError, Seq[Instruction]] =
  val lines = input.linesIterator.toSeq
  if lines.isEmpty then
    Left(AocError.ParseError(filename, "expected at least one screen instruction"))
  else
    lines.zipWithIndex.foldLeft[Either[AocError, Seq[Instruction]]](Right(Seq.empty)) {
      case (result, (line, index)) =>
        for
          instructions <- result
          instruction <- parseInstruction(filename, line, index + 1)
        yield instruction +: instructions
    }.map(_.reverse)
end parse

private def parseInstruction(
    filename: String,
    line: String,
    lineNumber: Int,
): Either[AocError, Instruction] = line match
  case RectPattern(width, height)          => parseRectangle(filename, lineNumber, width, height)
  case RotateRowPattern(row, amount)       => parseRowRotation(filename, lineNumber, row, amount)
  case RotateColumnPattern(column, amount) =>
    parseColumnRotation(filename, lineNumber, column, amount)
  case _ => Left(parseError(filename, lineNumber, "unrecognized screen instruction"))
end parseInstruction

private def parseRectangle(
    filename: String,
    lineNumber: Int,
    widthText: String,
    heightText: String,
): Either[AocError, Instruction] = (widthText.toIntOption, heightText.toIntOption) match
  case (Some(width), Some(height))
    if width > 0 && width <= ScreenWidth && height > 0 && height <= ScreenHeight =>
    Right(Instruction.Rect(width, height))
  case _ => Left(parseError(
      filename,
      lineNumber,
      s"expected a rectangle within ${ScreenWidth}x$ScreenHeight with positive dimensions",
    ))
end parseRectangle

private def parseRowRotation(
    filename: String,
    lineNumber: Int,
    rowText: String,
    amountText: String,
): Either[AocError, Instruction] = (rowText.toIntOption, amountText.toIntOption) match
  case (Some(row), Some(amount)) if row >= 0 && row < ScreenHeight =>
    Right(Instruction.RotateRow(row, amount))
  case _ => Left(parseError(
      filename,
      lineNumber,
      s"expected a row from 0 through ${ScreenHeight - 1} and a nonnegative rotation",
    ))
end parseRowRotation

private def parseColumnRotation(
    filename: String,
    lineNumber: Int,
    columnText: String,
    amountText: String,
): Either[AocError, Instruction] = (columnText.toIntOption, amountText.toIntOption) match
  case (Some(column), Some(amount)) if column >= 0 && column < ScreenWidth =>
    Right(Instruction.RotateColumn(column, amount))
  case _ => Left(parseError(
      filename,
      lineNumber,
      s"expected a column from 0 through ${ScreenWidth - 1} and a nonnegative rotation",
    ))
end parseColumnRotation

private def parseError(filename: String, lineNumber: Int, message: String): AocError.ParseError =
  AocError.ParseError(filename, s"line $lineNumber: $message")

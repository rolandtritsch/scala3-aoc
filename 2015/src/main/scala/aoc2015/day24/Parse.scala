package aoc2015.day24

import aoc2015.util.AocError

def parse(filename: String, input: String): Either[AocError, Seq[Int]] =
  val lines = input.linesIterator.toSeq
  lines.zipWithIndex.foldLeft[Either[AocError, Seq[Int]]](Right(Seq.empty)) {
    case (result, (line, index)) => result
        .flatMap(weights => parseLine(filename, line.trim, index + 1).map(weights :+ _))
  }
end parse

private def parseLine(filename: String, line: String, lineNum: Int): Either[AocError, Int] =
  if line.isEmpty then Left(AocError.ParseError(filename, s"empty line at $lineNum"))
  else
    try Right(line.toInt)
    catch
      case _: NumberFormatException =>
        Left(AocError.ParseError(filename, s"invalid weight '$line' at line $lineNum"))
end parseLine

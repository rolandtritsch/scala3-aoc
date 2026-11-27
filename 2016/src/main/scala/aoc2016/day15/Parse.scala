package aoc2016.day15

import aoc2016.util.AocError

private val DiscLine = """Disc #(\d+) has (\d+) positions; at time=0, it is at position (\d+)\.""".r

def parse(filename: String, input: String): Either[AocError, Seq[Disc]] =
  val lines = input.linesIterator.toSeq.filterNot(_.trim.isEmpty)
  if lines.isEmpty then Left(AocError.ParseError(filename, "expected at least one disc"))
  else
    lines.zipWithIndex.foldLeft[Either[AocError, Vector[Disc]]](Right(Vector.empty)) {
      case (result, (line, index)) =>
        for
          discs <- result
          disc <- parseLine(filename, line, index + 1)
        yield discs :+ disc
    }
end parse

private def parseLine(filename: String, line: String, lineNumber: Int): Either[AocError, Disc] =
  line.trim match
    case DiscLine(number, positions, start) =>
      for
        discNumber <- parsePositive(filename, lineNumber, "disc number", number)
        discPositions <- parsePositive(filename, lineNumber, "position count", positions)
        discStart <- parsePosition(filename, lineNumber, discPositions, start)
      yield Disc(discNumber, discPositions, discStart)
    case _ =>
      Left(AocError.ParseError(filename, s"line $lineNumber: unrecognized disc description"))
end parseLine

private def parsePositive(
    filename: String,
    lineNumber: Int,
    label: String,
    text: String,
): Either[AocError, Int] = text.toIntOption.filter(_ > 0).toRight(
  AocError.ParseError(filename, s"line $lineNumber: expected a positive $label; found '$text'")
)
end parsePositive

private def parsePosition(
    filename: String,
    lineNumber: Int,
    positions: Int,
    text: String,
): Either[AocError, Int] = text.toIntOption
  .filter(position => position >= 0 && position < positions).toRight(AocError.ParseError(
    filename,
    s"line $lineNumber: expected a start position in [0, $positions); found '$text'",
  ))
end parsePosition

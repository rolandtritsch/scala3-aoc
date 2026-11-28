package aoc2016.day16

import aoc2016.util.AocError

/** Parse exactly one non-empty row of binary digits, ignoring surrounding blank lines. */
def parse(filename: String, input: String): Either[AocError, Input] = input.linesIterator.toSeq
  .filterNot(_.trim.isEmpty) match
  case Seq(single) => parseRow(filename, single.trim).map(Input(_))
  case _           => Left(AocError.ParseError(filename, "expected exactly one row of bits"))

private def parseRow(filename: String, row: String): Either[AocError, Seq[Bit]] =
  if row.isEmpty then Left(AocError.ParseError(filename, "expected at least one binary digit"))
  else
    row.zipWithIndex.foldLeft[Either[AocError, Vector[Bit]]](Right(Vector.empty)) {
      case (result, (char, index)) =>
        for
          bits <- result
          bit <- bitFromChar(char).toRight(
            AocError
              .ParseError(filename, s"character ${index + 1}: expected '0' or '1'; found '$char'")
          )
        yield bits :+ bit
    }
end parseRow

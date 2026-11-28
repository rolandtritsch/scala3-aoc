package aoc2016.day18

import aoc2016.util.AocError

/** Parse exactly one non-empty row of safe/trap tiles, ignoring surrounding blank lines. */
def parse(filename: String, input: String): Either[AocError, Input] = input.linesIterator.toSeq
  .filterNot(_.trim.isEmpty) match
  case Seq(single) => parseRow(filename, single.trim).map(Input(_))
  case _           => Left(AocError.ParseError(filename, "expected exactly one row of tiles"))

private def parseRow(filename: String, row: String): Either[AocError, Seq[Tile]] =
  if row.isEmpty then Left(AocError.ParseError(filename, "expected at least one tile"))
  else
    row.zipWithIndex.foldLeft[Either[AocError, Vector[Tile]]](Right(Vector.empty)) {
      case (result, (char, index)) =>
        for
          tiles <- result
          tile <- tileFromChar(char).toRight(
            AocError
              .ParseError(filename, s"character ${index + 1}: expected '.' or '^'; found '$char'")
          )
        yield tiles :+ tile
    }
end parseRow

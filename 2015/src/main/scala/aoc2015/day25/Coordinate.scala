package aoc2015.day25

import aoc2015.util.AocError

/** A validated one-based position in the infinite code grid. */
opaque type Coordinate = (Int, Int)

object Coordinate:

  private val Message =
    """To continue, please consult the code grid in the manual\. {2}Enter the code at row ([0-9]+), column ([0-9]+)\."""
      .r

  def parse(filename: String, input: String): Either[AocError, Coordinate] = input.trim match
    case Message(rowText, columnText) => (rowText.toIntOption, columnText.toIntOption) match
        case (Some(row), Some(column)) if row > 0 && column > 0 => Right((row, column))
        case _ => Left(AocError.ParseError(filename, "row and column must be positive Int values"))
    case _ => Left(AocError.ParseError(
        filename,
        "expected \"To continue, please consult the code grid in the manual.  Enter the code at row R, column C.\"",
      ))
  end parse

  extension (coordinate: Coordinate)
    def row: Int = coordinate._1
    def column: Int = coordinate._2

end Coordinate

def parse(filename: String, input: String): Either[AocError, Coordinate] = Coordinate
  .parse(filename, input)

package aoc2015.day09

import aoc2015.util.AocError

/** A single undirected distance between two locations.
  *
  * The puzzle lists each pair of locations once; the distance applies in both directions.
  *
  * @param from
  *   one endpoint of the edge.
  * @param to
  *   the other endpoint of the edge.
  * @param distance
  *   the positive distance between the two locations.
  */
final case class Distance private (from: String, to: String, distance: Int)

object Distance:

  /** Parse one `A to B = N` line into a distance.
    *
    * The two location names must be distinct, and the distance must be a positive integer.
    *
    * @param filename
    *   the resource name, used for error messages.
    * @param line
    *   the raw input line.
    * @param lineNumber
    *   the one-based line number, used for error messages.
    * @return
    *   the parsed distance, or a parse error.
    */
  def parse(filename: String, line: String, lineNumber: Int = 1): Either[AocError, Distance] =
    def invalid(reason: String): Left[AocError, Nothing] =
      Left(AocError.ParseError(filename, s"line $lineNumber: $reason: $line"))

    line.trim.split("\\s+").toSeq match
      case Seq(from, "to", to, "=", distStr) =>
        if from == to then invalid("locations must differ")
        else
          distStr.toIntOption match
            case Some(d) if d > 0 => Right(Distance(from, to, d))
            case _                => invalid("distance must be a positive integer")
      case _ => invalid("expected \"A to B = N\"")
    end match

  end parse

end Distance

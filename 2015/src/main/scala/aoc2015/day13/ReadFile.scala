package aoc2015.day13

import aoc2015.util.AocError
import aoc2015.util.parseLines
import aoc2015.util.readResource
import zio.*

/** Load and validate Day13 input for the solution.
  *
  * The payload is the parsed list of preferences. Every ordered pair of distinct people must be
  * stated exactly once, and at least two people must be present, so that every circular seating is
  * a meaningful, fully-described arrangement.
  */
def readFile(filename: String): ZIO[Any, AocError, Seq[Preference]] = readResource(filename) {
  (name, input) =>
    parseLines(name, input)(Preference.parse).flatMap { preferences =>
      val names = preferences.flatMap(p => Seq(p.person, p.neighbor)).distinct
      val directedPairs = preferences.map(p => (p.person, p.neighbor))
      val expected = names.size * (names.size - 1)
      if names.size < 2 then
        Left(AocError.ValidationError(s"$name: at least two people are required"))
      else if directedPairs.distinct.size != directedPairs.size then
        Left(AocError.ValidationError(s"$name: duplicate preference for the same ordered pair"))
      else if directedPairs.size != expected then
        Left(AocError.ValidationError(s"$name: preferences do not cover every ordered pair"))
      else Right(preferences)
    }
}
end readFile

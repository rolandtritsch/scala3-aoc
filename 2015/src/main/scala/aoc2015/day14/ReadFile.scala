package aoc2015.day14

import aoc2015.util.AocError
import aoc2015.util.parseLines
import aoc2015.util.readResource
import zio.*

/** Load and validate Day14 input for the solution.
  *
  * The payload is the parsed list of reindeer. At least one reindeer must be present so the race
  * has a winner, and every reindeer must have a unique name.
  */
def readFile(filename: String): ZIO[Any, AocError, Seq[Reindeer]] = readResource(filename) {
  (name, input) =>
    parseLines(name, input)(Reindeer.parse).flatMap { reindeer =>
      val names = reindeer.map(_.name)
      if reindeer.isEmpty then
        Left(AocError.ValidationError(s"$name: at least one reindeer is required"))
      else if names.distinct.size != names.size then
        Left(AocError.ValidationError(s"$name: reindeer names must all be distinct"))
      else Right(reindeer)
    }
}
end readFile

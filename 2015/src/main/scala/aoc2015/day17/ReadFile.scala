package aoc2015.day17

import aoc2015.util.AocError
import aoc2015.util.parseLines
import aoc2015.util.readResource
import zio.*

/** Load and validate Day17 input for the solution.
  *
  * The payload is the list of container capacities, one per line, in liters. At least one container
  * must be present so there is something to fill.
  */
def readFile(filename: String): ZIO[Any, AocError, Seq[Liters]] = readResource(filename) {
  (name, input) =>
    parseLines(name, input)(Liters.parse).flatMap { capacities =>
      if capacities.isEmpty then
        Left(AocError.ValidationError(s"$name: at least one container is required"))
      else Right(capacities)
    }
}
end readFile

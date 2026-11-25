package aoc2015.day18

import aoc2015.util.AocError
import aoc2015.util.parseLines
import aoc2015.util.readResource
import zio.*

/** Load and validate Day18 input for the solution.
  *
  * The payload is a 100x100 grid of lights, one row per line. Each character is # (on) or . (off).
  */
def readFile(filename: String): ZIO[Any, AocError, Seq[Seq[Light]]] = readResource(filename) {
  (name, input) =>
    parseLines(name, input) { (filename, line, lineNumber) =>
      Light.parseLine(line, lineNumber).left.map(AocError.ParseError(name, _))
    }.flatMap { rows =>
      if rows.length != gridSide then
        Left(AocError.ValidationError(s"$name: expected $gridSide rows, got ${rows.length}"))
      else Right(rows)
    }
}
end readFile

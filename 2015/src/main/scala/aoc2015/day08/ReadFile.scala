package aoc2015.day08

import aoc2015.util.AocError
import aoc2015.util.parseLines
import aoc2015.util.readResource
import zio.*

/** Load and validate Day08 input for both parts.
  *
  * Each non-empty line must be a single double-quoted string literal. The payload is the parsed
  * sequence of `StringLiteral`s, one per line.
  */
def readFile(filename: String): ZIO[Any, AocError, Seq[StringLiteral]] =
  readResource(filename)((name, input) => parseLines(name, input)(StringLiteral.parse))

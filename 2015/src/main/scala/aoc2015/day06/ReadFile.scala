package aoc2015.day06

import aoc2015.util.AocError
import aoc2015.util.parseLines
import aoc2015.util.readResource
import zio.*

/** Load and validate Day06 input for both parts. */
def readFile(filename: String): ZIO[Any, AocError, Seq[Instruction]] =
  readResource(filename)((name, input) => parseLines(name, input)(Instruction.parse))

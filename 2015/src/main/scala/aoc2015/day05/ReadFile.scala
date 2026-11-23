package aoc2015.day05

import aoc2015.util.AocError
import aoc2015.util.parseLines
import aoc2015.util.readResource
import zio.*

/** Load and validate Day05 input for both parts. */
def readFile(filename: String): ZIO[Any, AocError, Seq[String]] =
  readResource(filename)((name, input) => parseLines(name, input)(parse))

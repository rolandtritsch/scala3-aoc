package aoc2015.day02

import aoc2015.util.AocError
import aoc2015.util.parseLines
import aoc2015.util.readResource
import zio.*

/** Load and validate Day02 input for both parts. */
def readFile(filename: String): ZIO[Any, AocError, Seq[Present]] =
  readResource(filename)((name, input) => parseLines(name, input)(Present.parseLine))

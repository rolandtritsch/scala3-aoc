package aoc2015.day03

import aoc2015.util.AocError
import aoc2015.util.readResource
import zio.*

/** Load and validate Day03 input for both parts. */
def readFile(filename: String): ZIO[Any, AocError, Seq[Direction]] = readResource(filename)(parse)

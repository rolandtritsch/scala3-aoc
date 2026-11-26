package aoc2015.day25

import aoc2015.util.AocError
import aoc2015.util.readResource
import zio.*

/** Load and validate the requested code-grid coordinate. */
def readFile(filename: String): ZIO[Any, AocError, Coordinate] = readResource(filename)(parse)

package aoc2016.day14

import aoc2016.util.AocError
import aoc2016.util.readResource
import zio.*

/** Load and validate the Day14 salt. */
def readFile(filename: String): ZIO[Any, AocError, Salt] = readResource(filename)(parse)

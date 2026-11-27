package aoc2016.day06

import aoc2016.util.AocError
import aoc2016.util.readResource
import zio.*

/** Load and validate the Day06 repeated signals. */
def readFile(filename: String): ZIO[Any, AocError, Recording] = readResource(filename)(parse)

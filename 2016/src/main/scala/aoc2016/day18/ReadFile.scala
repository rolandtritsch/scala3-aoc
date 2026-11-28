package aoc2016.day18

import aoc2016.util.AocError
import aoc2016.util.readResource
import zio.*

/** Load and validate the Day18 first row of tiles. */
def readFile(filename: String): ZIO[Any, AocError, Input] = readResource(filename)(parse)

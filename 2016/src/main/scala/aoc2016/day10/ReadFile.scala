package aoc2016.day10

import aoc2016.util.AocError
import aoc2016.util.readResource
import zio.*

/** Load and validate Day10 bot instructions. */
def readFile(filename: String): ZIO[Any, AocError, Factory] = readResource(filename)(parse)

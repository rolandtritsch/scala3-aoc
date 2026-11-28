package aoc2016.day21

import aoc2016.util.AocError
import aoc2016.util.readResource
import zio.*

/** Load and validate the Day21 scrambling operations. */
def readFile(filename: String): ZIO[Any, AocError, Input] = readResource(filename)(parse)

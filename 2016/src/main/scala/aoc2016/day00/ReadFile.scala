package aoc2016.day00

import aoc2016.util.AocError
import aoc2016.util.readResource
import zio.*

/** Load and validate Day00 input for both parts. */
def readFile(filename: String): ZIO[Any, AocError, Input] = readResource(filename)(parse)

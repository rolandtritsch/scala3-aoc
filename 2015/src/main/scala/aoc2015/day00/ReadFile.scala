package aoc2015.day00

import aoc2015.util.AocError
import aoc2015.util.readResource
import zio.*

/** Load and validate Day00 input for both parts. */
def readFile(filename: String): ZIO[Any, AocError, Input] = readResource(filename)(parse)

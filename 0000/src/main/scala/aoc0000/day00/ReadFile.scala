package aoc0000.day00

import aoc0000.util.AocError
import aoc0000.util.readResource
import zio.*

/** Load and validate Day00 input for both parts. */
def readFile(filename: String): ZIO[Any, AocError, Input] = readResource(filename)(parse)

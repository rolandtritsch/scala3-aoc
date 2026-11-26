package aoc2015.day20

import aoc2015.util.AocError
import aoc2015.util.readResource
import zio.*

/** Load and validate Day20 input. */
def readFile(filename: String): ZIO[Any, AocError, Input] = readResource(filename)(Input.parse)

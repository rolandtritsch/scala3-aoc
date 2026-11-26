package aoc2015.day21

import aoc2015.util.AocError
import aoc2015.util.readResource
import zio.*

/** Load and validate Day21 input. */
def readFile(filename: String): ZIO[Any, AocError, Input] = readResource(filename)(Input.parse)

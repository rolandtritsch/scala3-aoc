package aoc2016.day16

import aoc2016.util.AocError
import aoc2016.util.readResource
import zio.*

/** Load and validate the Day16 initial dragon-curve state. */
def readFile(filename: String): ZIO[Any, AocError, Input] = readResource(filename)(parse)

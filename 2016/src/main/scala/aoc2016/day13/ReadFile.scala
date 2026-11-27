package aoc2016.day13

import aoc2016.util.AocError
import aoc2016.util.readResource
import zio.*

/** Load and validate the Day13 favorite number. */
def readFile(filename: String): ZIO[Any, AocError, Input] = readResource(filename)(parse)

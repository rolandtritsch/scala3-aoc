package aoc2016.day11

import aoc2016.util.AocError
import aoc2016.util.readResource
import zio.*

/** Load and validate Day11 generator and microchip locations. */
def readFile(filename: String): ZIO[Any, AocError, Facility] = readResource(filename)(parse)

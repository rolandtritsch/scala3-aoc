package aoc2016.day05

import aoc2016.util.AocError
import aoc2016.util.readResource
import zio.*

/** Load and validate the Day05 Door ID. */
def readFile(filename: String): ZIO[Any, AocError, DoorId] = readResource(filename)(parse)

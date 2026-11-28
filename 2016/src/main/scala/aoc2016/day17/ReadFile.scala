package aoc2016.day17

import aoc2016.util.AocError
import aoc2016.util.readResource
import zio.*

/** Load and validate the Day17 passcode. */
def readFile(filename: String): ZIO[Any, AocError, Passcode] = readResource(filename)(parse)

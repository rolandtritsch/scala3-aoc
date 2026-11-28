package aoc2016.day22

import aoc2016.util.AocError
import aoc2016.util.readResource
import zio.*

/** Load and validate the Day22 storage cluster report. */
def readFile(filename: String): ZIO[Any, AocError, Input] = readResource(filename)(parse)

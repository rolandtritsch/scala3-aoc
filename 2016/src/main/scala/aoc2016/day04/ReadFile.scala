package aoc2016.day04

import aoc2016.util.AocError
import aoc2016.util.readResource
import zio.*

/** Load and validate Day04 rooms. */
def readFile(filename: String): ZIO[Any, AocError, Seq[Room]] = readResource(filename)(parse)

package aoc2015.day01

import aoc2015.util.AocError
import aoc2015.util.readResource
import zio.*

/** Load and validate Day01 input for both parts. */
def readFile(filename: String): ZIO[Any, AocError, Seq[Move]] = readResource(filename)(parse)

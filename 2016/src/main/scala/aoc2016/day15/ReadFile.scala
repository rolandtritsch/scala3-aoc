package aoc2016.day15

import aoc2016.util.AocError
import aoc2016.util.readResource
import zio.*

/** Load and validate Day15 discs. */
def readFile(filename: String): ZIO[Any, AocError, Seq[Disc]] = readResource(filename)(parse)

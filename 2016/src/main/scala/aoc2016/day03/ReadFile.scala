package aoc2016.day03

import aoc2016.util.AocError
import aoc2016.util.readResource
import zio.*

/** Load and validate Day03 triangles. */
def readFile(filename: String): ZIO[Any, AocError, Seq[Triangle]] = readResource(filename)(parse)

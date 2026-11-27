package aoc2016.day12

import aoc2016.util.AocError
import aoc2016.util.readResource
import zio.*

/** Load and validate Day12 assembunny instructions. */
def readFile(filename: String): ZIO[Any, AocError, Seq[Instruction]] = readResource(filename)(parse)

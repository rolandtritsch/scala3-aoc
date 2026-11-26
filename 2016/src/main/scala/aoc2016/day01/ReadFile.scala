package aoc2016.day01

import aoc2016.util.AocError
import aoc2016.util.readResource
import zio.*

/** Load and validate Day01 instructions. */
def readFile(filename: String): ZIO[Any, AocError, Seq[Instruction]] = readResource(filename)(parse)

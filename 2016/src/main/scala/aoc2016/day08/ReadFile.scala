package aoc2016.day08

import aoc2016.util.AocError
import aoc2016.util.readResource
import zio.*

/** Load and validate the Day08 screen instructions. */
def readFile(filename: String): ZIO[Any, AocError, Seq[Instruction]] = readResource(filename)(parse)

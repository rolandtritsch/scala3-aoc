package aoc2016.day19

import aoc2016.util.AocError
import aoc2016.util.readResource
import zio.*

/** Load and validate the Day19 Elf count. */
def readFile(filename: String): ZIO[Any, AocError, Input] = readResource(filename)(parse)

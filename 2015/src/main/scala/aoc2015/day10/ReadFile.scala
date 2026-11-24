package aoc2015.day10

import aoc2015.util.AocError
import aoc2015.util.readResource
import zio.*

/** Load and validate Day10 input for both parts. */
def readFile(filename: String): ZIO[Any, AocError, Digits] = readResource(filename)(Digits.parse)

package aoc2015.day04

import aoc2015.util.AocError
import aoc2015.util.readResource
import zio.*

/** Load and validate Day04 input for both parts. */
def readFile(filename: String): ZIO[Any, AocError, SecretKey] = readResource(filename)(parse)

package aoc2016.day09

import aoc2016.util.AocError
import aoc2016.util.readResource
import zio.*

/** Load and validate Day09 input for both parts. */
def readFile(filename: String): ZIO[Any, AocError, CompressedFile] = readResource(filename)(parse)

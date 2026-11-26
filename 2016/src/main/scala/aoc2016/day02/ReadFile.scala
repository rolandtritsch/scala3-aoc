package aoc2016.day02

import aoc2016.util.AocError
import aoc2016.util.readResource
import zio.*

/** Load and validate Day02 instructions. */
def readFile(filename: String): ZIO[Any, AocError, Seq[Seq[Direction]]] =
  readResource(filename)(parse)

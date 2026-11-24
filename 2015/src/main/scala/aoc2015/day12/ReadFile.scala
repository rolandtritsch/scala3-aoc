package aoc2015.day12

import aoc2015.util.AocError
import aoc2015.util.readResource
import zio.*

/** Load and validate the Day12 input for the solution.
  *
  * The payload is a single JSON document, parsed into a `Json` value before it reaches the pure
  * part functions.
  */
def readFile(filename: String): ZIO[Any, AocError, Json] = readResource(filename)(Json.parse)

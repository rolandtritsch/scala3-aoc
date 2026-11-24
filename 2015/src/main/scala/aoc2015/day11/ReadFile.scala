package aoc2015.day11

import aoc2015.util.AocError
import aoc2015.util.readResource
import zio.*

/** Load and validate Day11 input for the solution.
  *
  * The payload is a single parsed password of eight lowercase letters.
  */
def readFile(filename: String): ZIO[Any, AocError, Password] =
  readResource(filename)(Password.parse)

package aoc2016.day20

import aoc2016.util.AocError
import aoc2016.util.readResource
import zio.*

/** Load and validate the Day20 firewall blacklist. */
def readFile(filename: String): ZIO[Any, AocError, Input] = readResource(filename)(parse)

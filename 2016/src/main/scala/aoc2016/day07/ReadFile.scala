package aoc2016.day07

import aoc2016.util.AocError
import aoc2016.util.readResource
import zio.*

/** Load and validate the Day07 IPv7 addresses. */
def readFile(filename: String): ZIO[Any, AocError, Seq[IpAddress]] = readResource(filename)(parse)

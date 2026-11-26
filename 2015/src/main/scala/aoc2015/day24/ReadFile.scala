package aoc2015.day24

import aoc2015.util.AocError
import aoc2015.util.readResource
import zio.*

def readFile(filename: String): ZIO[Any, AocError, Seq[Int]] = readResource(filename)(parse)

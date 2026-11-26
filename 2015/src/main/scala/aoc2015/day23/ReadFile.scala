package aoc2015.day23

import aoc2015.util.AocError
import aoc2015.util.readResource
import zio.*

def readFile(filename: String): ZIO[Any, AocError, Seq[Instruction]] = readResource(filename)(parse)

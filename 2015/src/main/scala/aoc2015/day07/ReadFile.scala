package aoc2015.day07

import aoc2015.util.AocError
import aoc2015.util.parseLines
import aoc2015.util.readResource
import zio.*

/** Load and validate Day07 input for both parts.
  *
  * The payload is the parsed list of instructions. Duplicate output wires (a wire fed by more than
  * one source) are rejected because the puzzle guarantees exactly one source per wire.
  */
def readFile(filename: String): ZIO[Any, AocError, Seq[Instruction]] = readResource(filename) {
  (name, input) =>
    parseLines(name, input)(Instruction.parse).flatMap { instructions =>
      val duplicate = instructions.groupBy(_.output).find(_._2.size > 1)
      duplicate match
        case None            => Right(instructions)
        case Some((wire, _)) =>
          Left(AocError.ValidationError(s"$name: wire $wire assigned more than once"))
    }
}

end readFile

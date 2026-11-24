package aoc2015.day16

import aoc2015.util.AocError
import aoc2015.util.parseLines
import aoc2015.util.readResource
import zio.*

/** Load and validate Day16 input for the solution.
  *
  * The payload is the parsed list of Aunt Sues. At least one Sue must be present so the gift has a
  * plausible giver, and every Sue must have a unique number.
  */
def readFile(filename: String): ZIO[Any, AocError, Seq[AuntSue]] = readResource(filename) {
  (name, input) =>
    parseLines(name, input)(AuntSue.parse).flatMap { sues =>
      val numbers = sues.map(_.number)
      if sues.isEmpty then
        Left(AocError.ValidationError(s"$name: at least one Aunt Sue is required"))
      else if numbers.distinct.size != numbers.size then
        Left(AocError.ValidationError(s"$name: Aunt Sue numbers must all be distinct"))
      else Right(sues)
    }
}
end readFile

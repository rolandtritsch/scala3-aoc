package aoc0000.day00

import aoc0000.util.AocError

final case class Input(first: Index, last: Index)

/** At least one index is required; a single index selects the same Fibonacci number for both parts.
  */
def parse(filename: String, input: String): Either[AocError, Input] = input.linesIterator
  .zipWithIndex
  .foldLeft[Either[AocError, Seq[Index]]](Right(Seq.empty)) { case (result, (line, index)) =>
    for
      indices <- result
      parsed <- parseIndex(filename, line, index + 1)
    yield parsed +: indices
  }.flatMap { reversed =>
    (reversed.lastOption, reversed.headOption) match
      case (Some(first), Some(last)) => Right(Input(first, last))
      case _ => Left(AocError.ParseError(filename, "expected at least one Fibonacci index"))
  }
end parse

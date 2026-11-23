package aoc2015.day01

import aoc2015.util.AocError

/** Parse parentheses into moves, ignoring whitespace and rejecting other characters. */
def parse(filename: String, input: String): Either[AocError, Seq[Move]] = input.zipWithIndex
  .foldLeft[Either[AocError, Seq[Move]]](Right(Seq.empty)) { case (result, (character, index)) =>
    result.flatMap { moves =>
      character match
        case '('                 => Right(Move.Up +: moves)
        case ')'                 => Right(Move.Down +: moves)
        case c if c.isWhitespace => Right(moves)
        case c                   =>
          Left(AocError.ParseError(filename, s"unexpected character '$c' at position ${index + 1}"))
    }
  }.map(_.reverse)
end parse

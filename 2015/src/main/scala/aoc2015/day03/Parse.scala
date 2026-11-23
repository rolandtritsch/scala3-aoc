package aoc2015.day03

import aoc2015.util.AocError

/** Whitespace separates instructions and never consumes a mover's turn. */
def parse(filename: String, input: String): Either[AocError, Seq[Direction]] = input.zipWithIndex
  .foldLeft[Either[AocError, Seq[Direction]]](Right(Seq.empty)) {
    case (result, (character, index)) => result.flatMap { directions =>
        character match
          case '^'                 => Right(Direction.North +: directions)
          case 'v'                 => Right(Direction.South +: directions)
          case '>'                 => Right(Direction.East +: directions)
          case '<'                 => Right(Direction.West +: directions)
          case c if c.isWhitespace => Right(directions)
          case c                   => Left(
              AocError.ParseError(filename, s"unexpected character '$c' at position ${index + 1}")
            )
      }
  }.map(_.reverse)

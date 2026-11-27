package aoc2016.day14

import aoc2016.util.AocError

/** The pre-arranged salt that seeds the one-time pad key stream. */
opaque type Salt = String

extension (salt: Salt) def value: String = salt

/** Parse exactly one non-empty, whitespace-free salt line. */
def parse(filename: String, input: String): Either[AocError, Salt] = input.linesIterator.toSeq match
  case Seq(salt) if salt.nonEmpty && salt.forall(character => !character.isWhitespace) =>
    Right(salt)
  case Seq(_) =>
    Left(AocError.ParseError(filename, "line 1: expected a non-empty salt without whitespace"))
  case _ => Left(AocError.ParseError(filename, "expected exactly one salt line"))
end parse

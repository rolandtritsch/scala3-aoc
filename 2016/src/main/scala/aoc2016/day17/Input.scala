package aoc2016.day17

import aoc2016.util.AocError

opaque type Passcode = String

extension (passcode: Passcode) def value: String = passcode

/** Parse exactly one non-empty, whitespace-free passcode. */
def parse(filename: String, input: String): Either[AocError, Passcode] =
  input.linesIterator.toSeq match
    case Seq(passcode)
      if passcode.nonEmpty && passcode.forall(character => !character.isWhitespace) =>
      Right(passcode)
    case Seq(_) => Left(
        AocError.ParseError(filename, "line 1: expected a non-empty passcode without whitespace")
      )
    case _ => Left(AocError.ParseError(filename, "expected exactly one passcode line"))
end parse

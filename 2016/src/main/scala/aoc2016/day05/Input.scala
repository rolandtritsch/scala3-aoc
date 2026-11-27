package aoc2016.day05

import aoc2016.util.AocError

opaque type DoorId = String

extension (doorId: DoorId) def value: String = doorId

/** Parse exactly one non-empty, whitespace-free Door ID. */
def parse(filename: String, input: String): Either[AocError, DoorId] =
  input.linesIterator.toSeq match
    case Seq(doorId) if doorId.nonEmpty && doorId.forall(character => !character.isWhitespace) =>
      Right(doorId)
    case Seq(_) =>
      Left(AocError.ParseError(filename, "line 1: expected a non-empty Door ID without whitespace"))
    case _ => Left(AocError.ParseError(filename, "expected exactly one Door ID line"))
end parse

package aoc2015.day04

import aoc2015.util.AocError

opaque type SecretKey = String

/** The key is text, but its type guarantees it is one non-empty token. */
def parse(filename: String, input: String): Either[AocError, SecretKey] =
  val key = input.trim
  if key.nonEmpty && !key.exists(_.isWhitespace) then Right(key)
  else
    Left(
      AocError.ParseError(filename, "expected one non-empty secret key without internal whitespace")
    )
  end if

end parse

private[day04] def secretKeyValue(key: SecretKey): String = key

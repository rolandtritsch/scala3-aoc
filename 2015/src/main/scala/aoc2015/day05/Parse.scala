package aoc2015.day05

import aoc2015.util.AocError

/** Strings are the domain here. Blank lines are ignored; non-empty lines must be lowercase words.
  */
def parse(filename: String, line: String, lineNumber: Int = 1): Either[AocError, String] =
  val word = line.trim
  if word.nonEmpty && word.forall(c => c >= 'a' && c <= 'z') then Right(word)
  else
    Left(
      AocError.ParseError(filename, s"line $lineNumber: expected a non-empty lowercase word: $line")
    )
  end if

end parse

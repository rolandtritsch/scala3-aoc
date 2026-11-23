package aoc0000.day00

import aoc0000.util.AocError

opaque type Index = Int

def parseIndex(filename: String, line: String, lineNumber: Int = 1): Either[AocError, Index] = line
  .trim.toIntOption.filter(_ >= 0).toRight(AocError.ParseError(
    filename,
    s"line $lineNumber: expected a non-negative Int index: $line",
  ))

private[day00] def indexValue(index: Index): Int = index

package aoc2016.day19

import aoc2016.util.AocError

/** Parse exactly one non-empty positive Elf count, ignoring surrounding blank lines. */
def parse(filename: String, input: String): Either[AocError, Input] = input.linesIterator.toSeq
  .filterNot(_.trim.isEmpty) match
  case Seq(single) => single.trim.toIntOption.filter(_ > 0)
      .toRight(AocError.ParseError(filename, s"line 1: expected a positive Elf count: $single"))
      .map(Input(_))
  case _ => Left(AocError.ParseError(filename, "expected exactly one Elf count"))
end parse

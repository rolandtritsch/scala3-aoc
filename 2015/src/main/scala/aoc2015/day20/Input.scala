package aoc2015.day20

import aoc2015.util.AocError

/** Puzzle input: the target number of presents. */
opaque type Input = Int

object Input:
  def apply(target: Int): Input = target

  extension (input: Input) def target: Int = input

  def parse(filename: String, raw: String): Either[AocError, Input] = raw.linesIterator.map(_.trim)
    .filter(_.nonEmpty).toSeq match
    case Seq(line) => line.toIntOption
        .toRight(AocError.ParseError(filename, s"expected integer, got '$line'")).map(Input.apply)
    case _ => Left(AocError.ParseError(filename, "expected exactly one line with target presents"))

end Input

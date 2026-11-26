package aoc2015.day22

import aoc2015.util.AocError

/** Puzzle input: boss stats. */
final case class Input(hitPoints: Int, damage: Int)

object Input:

  def parse(filename: String, raw: String): Either[AocError, Input] =
    val lines = raw.linesIterator.map(_.trim).filter(_.nonEmpty).toSeq
    if lines.length != 2 then
      Left(AocError.ParseError(filename, "expected exactly two lines for boss stats"))
    else
      for
        hp <- parseLine(lines(0), "Hit Points")
        dmg <- parseLine(lines(1), "Damage")
      yield Input(hp, dmg)
  end parse

  private def parseLine(line: String, prefix: String): Either[AocError, Int] =
    if line.startsWith(s"$prefix: ") then
      line.drop(prefix.length + 2).toIntOption
        .toRight(AocError.ParseError("", s"expected integer after '$prefix: '"))
    else Left(AocError.ParseError("", s"expected line starting with '$prefix: '"))

end Input

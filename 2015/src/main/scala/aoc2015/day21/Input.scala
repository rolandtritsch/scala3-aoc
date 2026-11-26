package aoc2015.day21

import aoc2015.util.AocError

/** Puzzle input: boss stats. */
final case class Input(hitPoints: Int, damage: Int, armor: Int)

object Input:

  def parse(filename: String, raw: String): Either[AocError, Input] =
    val lines = raw.linesIterator.map(_.trim).filter(_.nonEmpty).toSeq
    if lines.length != 3 then
      Left(AocError.ParseError(filename, "expected exactly three lines for boss stats"))
    else
      for
        hp <- parseLine(lines(0), "Hit Points")
        dmg <- parseLine(lines(1), "Damage")
        arm <- parseLine(lines(2), "Armor")
      yield Input(hp, dmg, arm)
  end parse

  private def parseLine(line: String, prefix: String): Either[AocError, Int] =
    if line.startsWith(s"$prefix: ") then
      line.drop(prefix.length + 2).toIntOption
        .toRight(AocError.ParseError("", s"expected integer after '$prefix: '"))
    else Left(AocError.ParseError("", s"expected line starting with '$prefix: '"))

end Input

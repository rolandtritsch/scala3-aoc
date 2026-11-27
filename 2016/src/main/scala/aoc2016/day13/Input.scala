package aoc2016.day13

import aoc2016.util.AocError

/** The office designer's favorite number that seeds the cubicle maze layout. */
final case class Input(favoriteNumber: Int)

def parse(filename: String, input: String): Either[AocError, Input] =
  val content = input.linesIterator.toSeq.filterNot(_.trim.isEmpty)
  content match
    case Seq(single) => single.trim.toIntOption.filter(_ >= 0).toRight(AocError.ParseError(
        filename,
        s"line 1: expected a non-negative favorite number: $single",
      )).map(Input(_))
    case _ => Left(AocError.ParseError(filename, "expected exactly one favorite number"))
end parse

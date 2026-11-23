package aoc2015.day02

import aoc2015.util.AocError

final case class Present private (length: BigInt, width: BigInt, height: BigInt)

object Present:

  /** Dimensions are positive decimal integers. BigInt keeps areas and volumes exact. */
  def parseLine(filename: String, line: String, lineNumber: Int = 1): Either[AocError, Present] =
    line.trim.split("x", -1).toSeq match
      case Seq(l, w, h)
        if Seq(l, w, h).forall(s => s.nonEmpty && s.forall(c => c >= '0' && c <= '9')) =>
        val length = BigInt(l)
        val width = BigInt(w)
        val height = BigInt(h)
        if length > 0 && width > 0 && height > 0 then Right(Present(length, width, height))
        else
          Left(
            AocError.ParseError(filename, s"line $lineNumber: dimensions must be positive: $line")
          )
        end if
      case _ => Left(AocError.ParseError(filename, s"line $lineNumber: expected LxWxH: $line"))

end Present

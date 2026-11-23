package aoc2015

import aoc2015.util.AocError
import zio.*

/** Day02 - I Was Told There Would Be No Math. */
object Day02:

  final case class Present private[Day02] (length: BigInt, width: BigInt, height: BigInt)

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

  /** Blank lines are ignored; every present is validated before solving. */
  def readFile(filename: String): ZIO[Any, AocError, Seq[Present]] =
    import scala.io.Source

    ZIO.acquireReleaseWith(
      ZIO.attempt(Source.fromResource(filename)).mapError(AocError.FileReadError(filename, _))
    )(source => ZIO.succeed(source.close())) { source =>
      for
        _ <- ZIO.logDebug(s"filename: $filename")
        lines <- ZIO.attempt(source.getLines().toSeq).mapError(AocError.FileReadError(filename, _))
        presents <- ZIO.foreach(lines.zipWithIndex.filter(_._1.trim.nonEmpty)) { (line, index) =>
          for
            _ <- ZIO.logDebug(s"line ${index + 1}: $line")
            present <- ZIO.fromEither(parseLine(filename, line, index + 1))
            _ <- ZIO.logDebug(s"parsed: $present")
          yield present
        }
      yield presents
    }

  end readFile

  def wrappingPaper(present: Present): BigInt =
    val Present(l, w, h) = present
    val sides = Seq(l * w, w * h, h * l)
    2 * sides.sum + sides.min // Exactly three sides, so min is always defined.

  def ribbon(present: Present): BigInt =
    val Present(l, w, h) = present
    2 * (l + w + h - l.max(w).max(h)) + l * w * h

  def part1(presents: Seq[Present]): BigInt = presents.foldLeft(BigInt(0))(_ + wrappingPaper(_))

  def part2(presents: Seq[Present]): BigInt = presents.foldLeft(BigInt(0))(_ + ribbon(_))

end Day02

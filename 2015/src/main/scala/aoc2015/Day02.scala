package aoc2015

import zio.*

/** Day02 - I Was Told There Would Be No Math
  *
  * Each present is a perfect right rectangular prism described by its dimensions `length`, `width`,
  * and `height` (in feet). For each present the elves order exactly its surface area
  * `2*l*w + 2*w*h + 2*h*l` plus a little slack equal to the area of its smallest side.
  *
  * part1: the total square feet of wrapping paper to order, summed over every present.
  */
object Day02:

  /** @return
    *   the dimensions `(length, width, height)` parsed from `line`, which must have the form
    *   `l x w x h` with `x` as the separator and three positive integers. `filename` is only
    *   carried along for the error message.
    */
  def parseLine(filename: String, line: String): Either[AocError, (Int, Int, Int)] =
    val parts = line.split('x')
    if parts.length != 3 then
      Left(AocError.ParseError(filename, s"expected three dimensions separated by 'x' in: $line"))
    else
      (parts(0).toIntOption, parts(1).toIntOption, parts(2).toIntOption) match
        case (Some(l), Some(w), Some(h)) if l > 0 && w > 0 && h > 0 => Right((l, w, h))
        case _ => Left(AocError.ParseError(filename, s"expected three positive integers in: $line"))
    end if
  end parseLine

  /** @return
    *   the dimensions `(length, width, height)` parsed from each non-empty line of the given
    *   resource file, or an [[AocError]] if the resource is unreadable or a line is malformed.
    */
  def readFile(filename: String): ZIO[Any, AocError, Seq[(Int, Int, Int)]] =
    import scala.io.Source

    ZIO.acquireReleaseWith(
      ZIO.attempt(Source.fromResource(filename)).mapError(AocError.FileReadError(filename, _))
    )(source => ZIO.succeed(source.close())) { source =>
      val lines = source.getLines().map(_.trim).filter(_.nonEmpty).toSeq
      ZIO.logDebug(s"filename: $filename") *>
        ZIO.foreach(lines)(line => ZIO.fromEither(parseLine(filename, line)))
    }
  end readFile

  /** @return
    *   the square feet of wrapping paper needed for a single present: its surface area plus the
    *   area of its smallest side as slack.
    */
  def wrappingPaper(dimensions: (Int, Int, Int)): Int =
    val (l, w, h) = dimensions
    val sides = Seq(l * w, w * h, h * l)
    2 * sides.sum + sides.min
  end wrappingPaper

  /** @return
    *   the total wrapping paper needed for all presents: for each present, its surface area plus
    *   the area of its smallest side, summed over the whole list. Runs in O(n) time with O(1) extra
    *   space for n presents.
    */
  def part1(dimensions: Seq[(Int, Int, Int)]): Int = dimensions.map(wrappingPaper).sum
  end part1

  /** @return
    *   the feet of ribbon needed for a single present: the smallest perimeter of any one face
    *   (twice the sum of the two shortest sides) plus a bow equal to the present's volume.
    */
  def ribbon(dimensions: (Int, Int, Int)): Int =
    val (a, b, c) = dimensions
    val sorted = Seq(a, b, c).sorted
    2 * (sorted(0) + sorted(1)) + a * b * c
  end ribbon

  /** @return
    *   the total ribbon in feet needed for all presents: for each present, the smallest perimeter
    *   of any one face plus its volume (the bow), summed over the whole list. Runs in O(n) time
    *   with O(1) extra space for n presents.
    */
  def part2(dimensions: Seq[(Int, Int, Int)]): Int = dimensions.map(ribbon).sum
  end part2

end Day02

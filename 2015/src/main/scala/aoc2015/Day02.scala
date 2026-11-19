package aoc2015

import com.typesafe.scalalogging.Logger

/** Day02 - I Was Told There Would Be No Math
  *
  * Each present is a perfect right rectangular prism described by its dimensions `length`, `width`,
  * and `height` (in feet). For each present the elves order exactly its surface area
  * `2*l*w + 2*w*h + 2*h*l` plus a little slack equal to the area of its smallest side.
  *
  * part1: the total square feet of wrapping paper to order, summed over every present.
  */
object Day02:

  val logger: Logger = Logger(this.getClass.getName)

  /** @return
    *   the dimensions `(length, width, height)` parsed from each line of the given resource file,
    *   where every non-empty line has the form `l x w x h` with `x` as the separator.
    */
  def readFile(filename: String): Seq[(Int, Int, Int)] =
    import scala.io.Source

    require(filename.nonEmpty, "filename.nonEmpty")
    logger.debug(s"filename: ${filename}")

    val source = Source.fromResource(filename)
    try source.getLines().map(_.trim).filter(_.nonEmpty).toSeq.map: line =>
        logger.debug(s"line: ${line}")
        val parts = line.split('x')
        require(parts.length == 3, s"expected three dimensions separated by 'x' in: $line")
        (parts(0).toInt, parts(1).toInt, parts(2).toInt)
    finally source.close()
    end try
  end readFile

  /** @return
    *   the square feet of wrapping paper needed for a single present: its surface area plus the
    *   area of its smallest side as slack.
    */
  def wrappingPaper(dimensions: (Int, Int, Int)): Int =
    val (l, w, h) = dimensions
    require(l > 0 && w > 0 && h > 0, "dimensions must be positive")
    val sides = Seq(l * w, w * h, h * l)
    2 * sides.sum + sides.min
  end wrappingPaper

  /** @return
    *   the total wrapping paper needed for all presents: for each present, its surface area plus
    *   the area of its smallest side, summed over the whole list. Runs in O(n) time with O(1) extra
    *   space for n presents.
    */
  def part1(dimensions: Seq[(Int, Int, Int)]): Int =
    logger.debug(s"dimensions count: ${dimensions.length}")
    dimensions.map(wrappingPaper).sum
  end part1

  /** part2 is not assigned yet and must not be treated as solved. */
  def part2(dimensions: Seq[(Int, Int, Int)]): Int = ???
  end part2

end Day02

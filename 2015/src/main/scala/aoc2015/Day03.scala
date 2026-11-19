package aoc2015

import com.typesafe.scalalogging.Logger

/** Day03 - Perfectly Spherical Houses in a Vacuum
  *
  * Santa starts at one house on an infinite two-dimensional grid and then follows a sequence of
  * one-house moves: north (`^`), south (`v`), east (`>`), or west (`<`). After each move he
  * delivers a present to the house at his new location.
  *
  * part1: the number of distinct houses that receive at least one present.
  */
object Day03:

  val logger: Logger = Logger(this.getClass.getName)

  /** @return the direction string read from the given resource file. */
  def readFile(filename: String): String =
    import scala.io.Source

    require(filename.nonEmpty, "filename.nonEmpty")
    logger.debug(s"filename: ${filename}")

    val source = Source.fromResource(filename)
    try source.mkString
    finally source.close()
    end try
  end readFile

  /** @return
    *   the number of distinct houses that receive at least one present. Santa begins at the origin
    *   `(0, 0)`, which is counted as the first visited house, and then moves one grid unit per
    *   direction character. Every position he reaches is recorded in an immutable set, so houses
    *   visited more than once are counted only once. Unrecognized characters leave the position
    *   unchanged. Runs in O(n) time and O(n) extra space for n moves.
    */
  def part1(directions: String): Int =
    logger.debug(s"directions length: ${directions.length}")

    val origin = (0, 0)
    val visited = directions.foldLeft((Set(origin), origin)) { case ((houses, (x, y)), c) =>
      val next = c match
        case '^' => (x, y + 1)
        case 'v' => (x, y - 1)
        case '>' => (x + 1, y)
        case '<' => (x - 1, y)
        case _   => (x, y)
      (houses + next, next)
    }._1
    visited.size
  end part1

end Day03

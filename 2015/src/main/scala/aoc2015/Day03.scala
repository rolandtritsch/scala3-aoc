package aoc2015

import zio.*

/** Day03 - Perfectly Spherical Houses in a Vacuum
  *
  * Santa starts at one house on an infinite two-dimensional grid and then follows a sequence of
  * one-house moves: north (`^`), south (`v`), east (`>`), or west (`<`). After each move he
  * delivers a present to the house at his new location.
  *
  * part1: the number of distinct houses that receive at least one present.
  *
  * part2: Santa and Robo-Santa both start at the same house (which receives two presents, counted
  * once) and take turns moving, with Santa following the characters at even indices and Robo-Santa
  * the characters at odd indices. The answer is the number of distinct houses visited by either.
  */
object Day03:

  /** @return
    *   the direction string read from the given resource file, or an [[AocError]] if the resource
    *   is unreadable.
    */
  def readFile(filename: String): ZIO[Any, AocError, String] =
    import scala.io.Source

    ZIO.acquireReleaseWith(
      ZIO.attempt(Source.fromResource(filename)).mapError(AocError.FileReadError(filename, _))
    )(source => ZIO.succeed(source.close())) { source =>
      ZIO.logDebug(s"filename: $filename") *> ZIO.attempt(source.mkString)
        .mapError(AocError.FileReadError(filename, _))
    }
  end readFile

  /** @return
    *   the position reached after moving one grid unit from `pos` in the direction `c`, or `pos`
    *   unchanged for an unrecognized character.
    */
  private def step(pos: (Int, Int), c: Char): (Int, Int) =
    val (x, y) = pos
    c match
      case '^' => (x, y + 1)
      case 'v' => (x, y - 1)
      case '>' => (x + 1, y)
      case '<' => (x - 1, y)
      case _   => (x, y)
    end match
  end step

  /** @return
    *   the number of distinct houses that receive at least one present. Santa begins at the origin
    *   `(0, 0)`, which is counted as the first visited house, and then moves one grid unit per
    *   direction character. Every position he reaches is recorded in an immutable set, so houses
    *   visited more than once are counted only once. Unrecognized characters leave the position
    *   unchanged. Runs in O(n) time and O(n) extra space for n moves.
    */
  def part1(directions: String): Int =
    val origin = (0, 0)
    val visited = directions.foldLeft((Set(origin), origin)) { case ((houses, pos), c) =>
      val next = step(pos, c)
      (houses + next, next)
    }._1
    visited.size
  end part1

  /** @return
    *   the number of distinct houses that receive at least one present when Santa and Robo-Santa
    *   move on alternating instructions. Both start at the origin `(0, 0)`, which is delivered to
    *   once but counted as a single distinct house. Santa takes the characters at even indices and
    *   Robo-Santa the characters at odd indices. Their visited positions are merged into one
    *   immutable set, so houses reached by either (or both) are counted once. Runs in O(n) time and
    *   O(n) extra space for n moves.
    */
  def part2(directions: String): Int =
    val origin = (0, 0)
    val visited = directions.zipWithIndex
      .foldLeft((Set(origin), origin, origin)) { case ((houses, santa, robo), (c, i)) =>
        if i % 2 == 0 then
          val next = step(santa, c)
          (houses + next, next, robo)
        else
          val next = step(robo, c)
          (houses + next, santa, next)
      }._1
    visited.size
  end part2

end Day03

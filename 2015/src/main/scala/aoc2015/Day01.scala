package aoc2015

import zio.*

/** Day01 - Not Quite Lisp
  *
  * Santa starts on floor 0 and follows a sequence of parentheses, one at a time. An opening
  * parenthesis `(` means he goes up one floor; a closing parenthesis `)` means he goes down one
  * floor.
  *
  * part1: the final floor reached after following every instruction. part2: the 1-based position of
  * the first instruction that sends Santa into the basement (floor -1), or -1 if he never enters
  * the basement.
  */
object Day01:

  /** @return
    *   the instruction string read from the given resource file, or an [[AocError]] if the resource
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
    *   the floor reached by moving one unit from `floor` in the direction `c`, where `(` is +1 and
    *   `)` is -1, and any other character leaves the floor unchanged.
    */
  private def step(floor: Int, c: Char): Int = c match
    case '(' => floor + 1
    case ')' => floor - 1
    case _   => floor
  end step

  /** @return
    *   the running floor after each instruction, in order, with the starting floor 0 as the first
    *   element (before any instruction is applied). Shared by [[part1]] and [[part2]] so neither
    *   duplicates the scan: `part1` is its last element, `part2` is the position of its first -1.
    */
  private def floors(instructions: String): LazyList[Int] = instructions.to(LazyList)
    .scanLeft(0)(step)
  end floors

  /** @return the final floor after following every instruction. */
  def part1(instructions: String): Int = floors(instructions).last
  end part1

  /** @return
    *   the 1-based position of the first instruction that sends Santa into the basement (floor -1),
    *   or -1 if he never enters the basement. The index (after dropping the starting floor) of the
    *   first -1 in [[floors]] is exactly the count of instructions applied so far, i.e. the 1-based
    *   position of that instruction; `LazyList.indexWhere` already returns -1 when there is no
    *   match, matching the "never enters the basement" case directly.
    */
  def part2(instructions: String): Int = floors(instructions).drop(1).indexWhere(_ == -1) match
    case -1 => -1
    case i  => i + 1
  end part2

end Day01

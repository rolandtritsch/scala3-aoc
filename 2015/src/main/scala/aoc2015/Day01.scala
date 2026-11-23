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
    *   the final floor after following every instruction, where `(` is +1 and `)` is -1.
    */
  def part1(instructions: String): Int = instructions.foldLeft(0)((floor, c) =>
    c match
      case '(' => floor + 1
      case ')' => floor - 1
      case _   => floor
  )
  end part1

  /** @return
    *   the 1-based position of the first instruction that sends Santa into the basement (floor -1),
    *   or -1 if he never enters the basement. Scans the instructions once, tracking the running
    *   floor until it first reaches -1; `(` is +1 and `)` is -1. The scan is tail-recursive so no
    *   `var` tracks the running floor or position.
    */
  def part2(instructions: String): Int =
    @scala.annotation.tailrec
    def go(i: Int, floor: Int): Int =
      if i >= instructions.length then -1
      else
        val nextFloor = instructions(i) match
          case '(' => floor + 1
          case ')' => floor - 1
          case _   => floor
        if nextFloor == -1 then i + 1 else go(i + 1, nextFloor)

    go(0, 0)
  end part2

end Day01

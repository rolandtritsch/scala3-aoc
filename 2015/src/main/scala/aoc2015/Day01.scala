package aoc2015

import com.typesafe.scalalogging.Logger

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
  val logger: Logger = Logger(this.getClass.getName)

  /** @return the instruction string read from the given resource file. */
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
    *   the final floor after following every instruction, where `(` is +1 and `)` is -1.
    */
  def part1(instructions: String): Int =
    logger.debug(s"instructions length: ${instructions.length}")

    instructions.foldLeft(0)((floor, c) =>
      c match
        case '(' => floor + 1
        case ')' => floor - 1
        case _   => floor
    )
  end part1

  /** @return
    *   the 1-based position of the first instruction that sends Santa into the basement (floor -1),
    *   or -1 if he never enters the basement. Scans the instructions once, tracking the running
    *   floor until it first reaches -1; `(` is +1 and `)` is -1.
    */
  def part2(instructions: String): Int =
    logger.debug(s"instructions length: ${instructions.length}")

    var floor = 0
    var position = 0
    var enteringBasement = false
    for c <- instructions if !enteringBasement do
      c match
        case '(' => floor += 1
        case ')' => floor -= 1
        case _   => ()
      position += 1
      if floor == -1 then enteringBasement = true
    end for

    if enteringBasement then position else -1
  end part2

end Day01

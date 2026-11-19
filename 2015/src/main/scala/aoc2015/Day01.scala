package aoc2015

import com.typesafe.scalalogging.Logger

/** Day01 - Not Quite Lisp
  *
  * Santa starts on floor 0 and follows a sequence of parentheses, one at a time. An opening
  * parenthesis `(` means he goes up one floor; a closing parenthesis `)` means he goes down one
  * floor.
  *
  * part1: the final floor reached after following every instruction. part2: (not yet assigned) the
  * position of the first instruction that sent Santa into the basement (floor -1).
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

end Day01

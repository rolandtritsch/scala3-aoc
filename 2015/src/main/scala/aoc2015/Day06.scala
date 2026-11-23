package aoc2015

import com.typesafe.scalalogging.Logger

/** Day06 - Probably a Fire Hazard
  *
  * Santa deploys one million lights in a 1000x1000 grid, each numbered from 0 to 999 in each
  * direction, and follows a sequence of instructions that turn lights on, turn them off, or toggle
  * them. Every instruction names an inclusive rectangular range, given as the coordinates of two
  * opposite corners, and all lights start switched off.
  *
  * part1: after following all the instructions in order, the number of lights that are on.
  *
  * part2: the fine print explains that `turn on` actually increases a light's brightness by one,
  * `turn off` decreases it by one (to a minimum of zero), and `toggle` increases it by two. The
  * answer is the total brightness of all the lights added together.
  */
object Day06:

  val logger: Logger = Logger(this.getClass.getName)

  private val gridSide: Int = 1000

  /** The operation an instruction applies to every light inside its range. */
  enum Action:
    case TurnOn, TurnOff, Toggle
  end Action

  private val instructionPattern =
    """^(turn on|turn off|toggle) (\d+),(\d+) through (\d+),(\d+)$""".r

  /** @return
    *   the instructions read from the given resource file, one per non-empty line, with surrounding
    *   whitespace removed.
    */
  def readFile(filename: String): Seq[String] =
    import scala.io.Source

    require(filename.nonEmpty, "filename.nonEmpty")
    logger.debug(s"filename: ${filename}")

    val source = Source.fromResource(filename)
    try source.getLines().map(_.trim).filter(_.nonEmpty).toSeq
    finally source.close()
    end try
  end readFile

  /** @return
    *   the action and the inclusive opposite corners `(x1, y1)` and `(x2, y2)` parsed from `line`.
    */
  def parse(line: String): (Action, Int, Int, Int, Int) =
    line match
      case instructionPattern(op, x1, y1, x2, y2) =>
        val action = op match
          case "turn on"  => Action.TurnOn
          case "turn off" => Action.TurnOff
          case "toggle"   => Action.Toggle
        (action, x1.toInt, y1.toInt, x2.toInt, y2.toInt)
      case _ => throw new IllegalArgumentException(s"Unrecognized instruction: $line")
    end match
  end parse

  /** @return
    *   the number of lights that are on after applying every instruction in order. Lights start
    *   off; `turn on` switches a light on, `turn off` switches it off, and `toggle` flips it. Each
    *   instruction updates every cell inside its inclusive rectangle, so the grid is stored as a
    *   mutable array of `gridSide * gridSide` booleans and the answer is the number of `true`
    *   entries. Runs in O(n * A) time for n instructions covering on average A cells, using O(1)
    *   extra space beyond the grid.
    */
  def part1(instructions: Seq[String]): Int =
    logger.debug(s"instructions count: ${instructions.length}")

    val lights = Array.fill[Boolean](gridSide * gridSide)(false)
    instructions.foreach { line =>
      val (action, x1, y1, x2, y2) = parse(line)
      var y = y1
      while y <= y2 do
        var x = x1
        while x <= x2 do
          val i = y * gridSide + x
          action match
            case Action.TurnOn  => lights(i) = true
            case Action.TurnOff => lights(i) = false
            case Action.Toggle  => lights(i) = !lights(i)
          end match
          x += 1
        end while
        y += 1
      end while
    }
    lights.count(identity)
  end part1

  /** @return
    *   the total brightness of all lights after applying every instruction in order under the
    *   revised semantics. Lights start at brightness zero; `turn on` adds one, `turn off` subtracts
    *   one but never below zero, and `toggle` adds two. The grid is stored as a mutable array of
    *   `gridSide * gridSide` integers and the answer is their sum. Runs in O(n * A) time for n
    *   instructions covering on average A cells, using O(1) extra space beyond the grid.
    */
  def part2(instructions: Seq[String]): Int =
    logger.debug(s"instructions count: ${instructions.length}")

    val lights = Array.fill[Int](gridSide * gridSide)(0)
    instructions.foreach { line =>
      val (action, x1, y1, x2, y2) = parse(line)
      var y = y1
      while y <= y2 do
        var x = x1
        while x <= x2 do
          val i = y * gridSide + x
          action match
            case Action.TurnOn  => lights(i) += 1
            case Action.TurnOff => lights(i) = math.max(0, lights(i) - 1)
            case Action.Toggle  => lights(i) += 2
          end match
          x += 1
        end while
        y += 1
      end while
    }
    lights.sum
  end part2

end Day06

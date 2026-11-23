package aoc2015

import zio.*

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

  private val gridSide: Int = 1000

  /** The operation an instruction applies to every light inside its range. */
  enum Action:
    case TurnOn, TurnOff, Toggle
  end Action

  private val instructionPattern =
    """^(turn on|turn off|toggle) (\d+),(\d+) through (\d+),(\d+)$""".r

  /** @return
    *   the instructions read from the given resource file, one per non-empty line, with surrounding
    *   whitespace removed, or an [[AocError]] if the resource is unreadable.
    */
  def readFile(filename: String): ZIO[Any, AocError, Seq[String]] =
    import scala.io.Source

    ZIO.acquireReleaseWith(
      ZIO.attempt(Source.fromResource(filename)).mapError(AocError.FileReadError(filename, _))
    )(source => ZIO.succeed(source.close())) { source =>
      ZIO.attempt(source.getLines().map(_.trim).filter(_.nonEmpty).toSeq)
        .mapError(AocError.FileReadError(filename, _))
    }
  end readFile

  /** @return
    *   the action and the inclusive opposite corners `(x1, y1)` and `(x2, y2)` parsed from `line`,
    *   or an [[AocError]] if `line` does not match an instruction.
    */
  def parse(line: String): Either[AocError, (Action, Int, Int, Int, Int)] =
    line match
      case instructionPattern(op, x1, y1, x2, y2) =>
        val action = op match
          case "turn on"  => Action.TurnOn
          case "turn off" => Action.TurnOff
          case "toggle"   => Action.Toggle
        Right((action, x1.toInt, y1.toInt, x2.toInt, y2.toInt))
      case _ => Left(AocError.ParseError("", s"Unrecognized instruction: $line"))
    end match
  end parse

  /** Applies `action` to every cell of `lights` inside the inclusive rectangle `(x1, y1)` to
    * `(x2, y2)`, mutating the grid in place. `lights` is a local O(1)-space accumulator, not a
    * `var`; the nested range iterates without any loop counter variable.
    */
  private def applyRange(
      lights: Array[Boolean],
      action: Action,
      x1: Int,
      y1: Int,
      x2: Int,
      y2: Int,
  ): Unit = for
    y <- y1 to y2
    x <- x1 to x2
  do
    val i = y * gridSide + x
    action match
      case Action.TurnOn  => lights(i) = true
      case Action.TurnOff => lights(i) = false
      case Action.Toggle  => lights(i) = !lights(i)
    end match
  end applyRange

  /** Adds `delta` to every cell of `lights` inside the inclusive rectangle `(x1, y1)` to
    * `(x2, y2)`, clamped at a minimum of zero, mutating the grid in place.
    */
  private def applyBrightness(
      lights: Array[Int],
      delta: Int,
      x1: Int,
      y1: Int,
      x2: Int,
      y2: Int,
  ): Unit = for
    y <- y1 to y2
    x <- x1 to x2
  do
    val i = y * gridSide + x
    lights(i) = math.max(0, lights(i) + delta)
  end applyBrightness

  /** @return
    *   the number of lights that are on after applying every instruction in order, or an
    *   [[AocError]] if any instruction fails to parse. Lights start off; `turn on` switches a light
    *   on, `turn off` switches it off, and `toggle` flips it. Each instruction updates every cell
    *   inside its inclusive rectangle, so the grid is stored as a mutable array of
    *   `gridSide * gridSide` booleans and the answer is the number of `true` entries. Runs in O(n *
    *   A) time for n instructions covering on average A cells, using O(1) extra space beyond the
    *   grid.
    */
  def part1(instructions: Seq[String]): Either[AocError, Int] =
    val lights = Array.fill[Boolean](gridSide * gridSide)(false)
    instructions.foldLeft[Either[AocError, Unit]](Right(())) { (acc, line) =>
      for
        _ <- acc
        (action, x1, y1, x2, y2) <- parse(line)
      yield applyRange(lights, action, x1, y1, x2, y2)
    }.map(_ => lights.count(identity))
  end part1

  /** @return
    *   the total brightness of all lights after applying every instruction in order under the
    *   revised semantics, or an [[AocError]] if any instruction fails to parse. Lights start at
    *   brightness zero; `turn on` adds one, `turn off` subtracts one but never below zero, and
    *   `toggle` adds two. The grid is stored as a mutable array of `gridSide * gridSide` integers
    *   and the answer is their sum. Runs in O(n * A) time for n instructions covering on average A
    *   cells, using O(1) extra space beyond the grid.
    */
  def part2(instructions: Seq[String]): Either[AocError, Int] =
    val lights = Array.fill[Int](gridSide * gridSide)(0)
    instructions.foldLeft[Either[AocError, Unit]](Right(())) { (acc, line) =>
      for
        _ <- acc
        (action, x1, y1, x2, y2) <- parse(line)
        delta = action match
          case Action.TurnOn  => 1
          case Action.TurnOff => -1
          case Action.Toggle  => 2
      yield applyBrightness(lights, delta, x1, y1, x2, y2)
    }.map(_ => lights.sum)
  end part2

end Day06

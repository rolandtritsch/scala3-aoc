package aoc2015.day17

import aoc2015.util.AocError

/** A container's capacity in whole liters, the domain value for Day17.
  *
  * The puzzle input lists one capacity per line. A capacity is a positive integer: zero or a
  * negative value cannot hold eggnog and is rejected while parsing.
  */
opaque type Liters = Int

object Liters:

  /** Wrap `value` as a capacity, or `None` when it is not a positive integer. */
  def apply(value: Int): Option[Liters] = Option.when(value > 0)(value)

  /** The whole-liters value of a capacity. */
  def value(liters: Liters): Int = liters

  /** Parse one input line as a single positive whole-liters capacity.
    *
    * @param filename
    *   the resource name, used for error messages.
    * @param line
    *   the raw input line.
    * @param lineNumber
    *   the one-based line number, used for error messages.
    * @return
    *   the parsed capacity, or a parse error.
    */
  def parse(filename: String, line: String, lineNumber: Int = 1): Either[AocError, Liters] =
    line.trim.toIntOption match
      case Some(value) if value > 0 => Right(value)
      case _                        => Left(AocError.ParseError(
          filename,
          s"line $lineNumber: expected a positive whole-liters capacity: $line",
        ))
    end match
  end parse

end Liters

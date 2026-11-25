package aoc2015.day18

/** A light in the grid, either on or off. */
opaque type Light = Boolean

object Light:

  /** Create a light from a character (# = on, . = off). */
  def apply(char: Char): Option[Light] = PartialFunction
    .condOpt(char) { case '#' => true; case '.' => false }

  /** Create a light from a boolean value. */
  def apply(value: Boolean): Light = value

  /** The boolean value of a light. */
  def value(light: Light): Boolean = light

  /** Parse a single line of the grid into a sequence of lights. */
  def parseLine(line: String, lineNumber: Int): Either[String, Seq[Light]] =
    if line.length != gridSide then
      Left(s"line $lineNumber: expected $gridSide characters, got ${line.length}")
    else
      val lights = line.map(apply).toSeq
      if lights.contains(None) then
        Left(s"line $lineNumber: invalid character in grid (only # and . allowed)")
      else Right(lights.flatten)
  end parseLine

  /** Count the number of lights that are on in a grid. */
  def countOn(grid: Seq[Seq[Light]]): Int = grid.flatten.count(identity)

end Light

package aoc2015.day06

private def brightness(current: Long, action: Action): Long = action match
  case Action.TurnOn  => current + 1
  case Action.TurnOff => (current - 1).max(0L)
  case Action.Toggle  => current + 2

/** Long brightness and totals avoid Int overflow across large instruction sequences. A local array
  * provides constant-time cell updates, as in part1.
  */
def part2(instructions: Seq[Instruction]): Long =
  val lights = Array.fill[Long](gridSide * gridSide)(0L)
  for
    instruction <- instructions
    y <- instruction.from.y to instruction.to.y
    x <- instruction.from.x to instruction.to.x
  do
    val index = y * gridSide + x // Parsed corners guarantee a valid array index.
    lights(index) = brightness(lights(index), instruction.action)
  end for
  lights.sum

end part2

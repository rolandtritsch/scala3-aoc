package aoc2015.day06

private def switched(on: Boolean, action: Action): Boolean = action match
  case Action.TurnOn  => true
  case Action.TurnOff => false
  case Action.Toggle  => !on

/** Arrays provide constant-time cell updates without copying the million-cell grid. Mutation stays
  * local, so evaluating either part cannot change its input or another run.
  */
def part1(instructions: Seq[Instruction]): Int =
  val lights = Array.fill[Boolean](gridSide * gridSide)(false)
  for
    instruction <- instructions
    y <- instruction.from.y to instruction.to.y
    x <- instruction.from.x to instruction.to.x
  do
    val index = y * gridSide + x // Parsed corners guarantee a valid array index.
    lights(index) = switched(lights(index), instruction.action)
  end for
  lights.count(identity)

end part1

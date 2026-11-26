package aoc2016.day01

def part1(input: Seq[Instruction]): Int =
  val destination = input.foldLeft(State(Heading.North, 0, 0))(advance)
  destination.x.abs + destination.y.abs

private def turn(heading: Heading, direction: Turn): Heading = (heading, direction) match
  case (Heading.North, Turn.Left)  => Heading.West
  case (Heading.North, Turn.Right) => Heading.East
  case (Heading.East, Turn.Left)   => Heading.North
  case (Heading.East, Turn.Right)  => Heading.South
  case (Heading.South, Turn.Left)  => Heading.East
  case (Heading.South, Turn.Right) => Heading.West
  case (Heading.West, Turn.Left)   => Heading.South
  case (Heading.West, Turn.Right)  => Heading.North
end turn

private def advance(state: State, instruction: Instruction): State =
  val heading = turn(state.heading, instruction.turn)
  heading match
    case Heading.North => state.copy(heading = heading, y = state.y + instruction.blocks)
    case Heading.East  => state.copy(heading = heading, x = state.x + instruction.blocks)
    case Heading.South => state.copy(heading = heading, y = state.y - instruction.blocks)
    case Heading.West  => state.copy(heading = heading, x = state.x - instruction.blocks)
end advance

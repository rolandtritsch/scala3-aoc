package aoc2016.day01

private final case class State(heading: Heading, x: Int, y: Int)

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
  forward(state.copy(heading = heading), instruction.blocks)

private def forward(state: State, blocks: Int): State = state.heading match
  case Heading.North => state.copy(y = state.y + blocks)
  case Heading.East  => state.copy(x = state.x + blocks)
  case Heading.South => state.copy(y = state.y - blocks)
  case Heading.West  => state.copy(x = state.x - blocks)

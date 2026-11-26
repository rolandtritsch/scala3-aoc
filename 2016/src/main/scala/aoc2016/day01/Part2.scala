package aoc2016.day01

import scala.annotation.tailrec

def part2(input: Seq[Instruction]): Option[Int] =
  // A set provides constant-time membership checks for each block on the path.
  search(input, State(Heading.North, 0, 0), 0, Set((0, 0)))

@tailrec
private def search(
    input: Seq[Instruction],
    state: State,
    blocksRemaining: Int,
    visited: Set[(Int, Int)],
): Option[Int] =
  if blocksRemaining == 0 then
    input match
      case Seq()               => None
      case instruction +: rest =>
        val heading = turn(state.heading, instruction.turn)
        search(rest, state.copy(heading = heading), instruction.blocks, visited)
  else
    val next = forward(state, 1)
    val location = (next.x, next.y)
    if visited.contains(location) then Some(next.x.abs + next.y.abs)
    else search(input, next, blocksRemaining - 1, visited + location)
end search

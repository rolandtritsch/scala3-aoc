package aoc2016.day01

def part1(input: Seq[Instruction]): Int =
  val destination = input.foldLeft(State(Heading.North, 0, 0))(advance)
  destination.x.abs + destination.y.abs

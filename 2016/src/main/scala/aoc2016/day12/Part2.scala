package aoc2016.day12

def part2(instructions: Seq[Instruction]): Int =
  execute(instructions, Registers(a = 0, b = 0, c = 1, d = 0)).a

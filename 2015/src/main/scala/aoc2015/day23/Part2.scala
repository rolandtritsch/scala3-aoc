package aoc2015.day23

def part2(instructions: Seq[Instruction]): Int = execute(instructions, Registers(a = 1)).b

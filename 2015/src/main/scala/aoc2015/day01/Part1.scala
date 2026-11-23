package aoc2015.day01

/** @return the final floor after following every instruction. */
def part1(instructions: Seq[Move]): Int = floors(instructions).last

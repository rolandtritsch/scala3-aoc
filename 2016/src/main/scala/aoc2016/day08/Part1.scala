package aoc2016.day08

/** Count the lit pixels after applying every instruction to the 50 by 6 screen. */
def part1(instructions: Seq[Instruction]): Int = runScreen(ScreenWidth, ScreenHeight, instructions)
  .lit.size

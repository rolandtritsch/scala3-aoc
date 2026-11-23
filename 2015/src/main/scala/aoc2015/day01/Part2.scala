package aoc2015.day01

/** @return
  *   the 1-based position of the first instruction that sends Santa into the basement (floor -1),
  *   or -1 if he never enters the basement. Since [[floors]] includes the starting floor at index
  *   0, each subsequent index equals the number of instructions applied. `LazyList.indexWhere`
  *   returns -1 when there is no match.
  */
def part2(instructions: Seq[Move]): Int = floors(instructions).indexWhere(_ == -1)

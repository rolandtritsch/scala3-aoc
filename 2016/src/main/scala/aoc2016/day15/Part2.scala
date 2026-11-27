package aoc2016.day15

/** Part two adds one more disc, with 11 positions starting at position 0, exactly one second below
  * the previously-bottom disc. Its ordinal continues the sequence, so it is reached one second
  * after the last existing disc.
  */
def part2(discs: Seq[Disc]): Int = part1(discs :+ Disc(discs.size + 1, 11, 0))

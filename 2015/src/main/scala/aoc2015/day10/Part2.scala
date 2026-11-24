package aoc2015.day10

/** The length of the look-and-say sequence after applying the process 50 times.
  *
  * Part two uses the same look-and-say iteration as part one, but for fifty steps instead of forty.
  * The answer is the number of digits in the sequence produced by the 50th iteration.
  */
def part2(digits: Digits): Int =
  val result = (1 to 50).foldLeft(digits)((sequence, _) => lookAndSay(sequence))
  Digits.length(result)

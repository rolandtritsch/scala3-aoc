package aoc2015.day10

/** The length of the look-and-say sequence after applying the process 40 times.
  *
  * Each iteration replaces every run of identical digits with its length (in decimal) followed by
  * the digit itself. The answer is the number of digits in the sequence produced by the 40th
  * iteration.
  */
def part1(digits: Digits): Int =
  val result = (1 to 40).foldLeft(digits)((sequence, _) => lookAndSay(sequence))
  Digits.length(result)

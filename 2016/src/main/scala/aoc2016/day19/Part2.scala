package aoc2016.day19

import scala.annotation.tailrec

/** The part-two answer: the number of the Elf that ends up with every present.
  *
  * Each Elf steals from the one directly across the circle, taking the left one when two sit
  * opposite. Writing `p` for the largest power of three not exceeding `n`, the survivor sits at
  * 1-based position `n` when `n == p`, `n - p` when `p < n <= 2p`, and `2n - 3p` when
  * `2p < n < 3p`.
  */
def part2(input: Input): Int = survivingElf(input.elfCount)

private def survivingElf(elfCount: Int): Int =
  val power = highestPowerOfThree(elfCount)
  if elfCount == power then elfCount
  else if elfCount <= 2 * power then elfCount - power
  else 2 * elfCount - 3 * power

/** The largest power of three not exceeding `value` (at least 1 for a positive value). */
private def highestPowerOfThree(value: Int): Int =
  @tailrec
  def loop(power: Int): Int = if power > value / 3 then power else loop(power * 3)
  loop(1)

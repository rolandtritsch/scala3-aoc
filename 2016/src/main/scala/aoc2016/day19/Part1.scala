package aoc2016.day19

import scala.annotation.tailrec

/** The part-one answer: the number of the Elf that ends up with every present.
  *
  * This is the Josephus problem with step two: repeatedly the current Elf removes the next one.
  * Writing the count as `n = 2^m + L` with `0 <= L < 2^m`, the survivor sits at 1-based position
  * `2L + 1`.
  */
def part1(input: Input): Int = winningElf(input.elfCount)

private def winningElf(elfCount: Int): Int =
  val power = highestPowerOfTwo(elfCount)
  2 * (elfCount - power) + 1

/** The largest power of two not exceeding `value` (at least 1 for a positive value). */
private def highestPowerOfTwo(value: Int): Int =
  @tailrec
  def loop(power: Int): Int = if power > value / 2 then power else loop(power * 2)
  loop(1)

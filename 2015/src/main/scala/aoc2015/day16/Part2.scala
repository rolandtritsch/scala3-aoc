package aoc2015.day16

import aoc2015.util.AocError

/** The number of the single Aunt Sue whose remembered facts match the range-based MFCSAM reading.
  *
  * The outdated retroencabulator makes some readings ranges rather than exact values: cats and
  * trees are read low, so the Sue remembers more of them than the reading, while pomeranians and
  * goldfish are read high, so the Sue remembers fewer. Every other attribute still has to match
  * exactly, and an attribute the Sue does not remember never rules her out. The puzzle guarantees
  * exactly one such Sue, so the search always finds one; a missing match is reported as a
  * validation error.
  */
def part2(sues: Seq[AuntSue]): Either[AocError, Int] = sues.find(consistentWithRanges).map(_.number)
  .toRight(AocError.ValidationError("no Aunt Sue matches the range-based ticker tape"))

/** Whether every fact a Sue remembers satisfies the range-based MFCSAM reading.
  *
  * Cats and trees must be greater than their reading, pomeranians and goldfish fewer than their
  * reading, and every other attribute exactly equal. An attribute the Sue does not remember is
  * unknown and never contradicts the tape.
  */
def consistentWithRanges(sue: AuntSue): Boolean = sue.remembered.forall { (attribute, count) =>
  tickerTape.get(attribute).exists { reading =>
    attribute match
      case Attribute.Cats | Attribute.Trees           => count > reading
      case Attribute.Pomeranians | Attribute.Goldfish => count < reading
      case _                                          => count == reading
  }
}
end consistentWithRanges

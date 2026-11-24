package aoc2015.day16

import aoc2015.util.AocError

/** The number of the single Aunt Sue whose remembered facts match the ticker tape.
  *
  * Part one keeps a Sue only when every remembered count equals the MFCSAM reading; a fact she does
  * not remember never rules her out. The puzzle guarantees exactly one such Sue, so the search
  * always finds one; a missing match is reported as a validation error instead of a partial lookup.
  */
def part1(sues: Seq[AuntSue]): Either[AocError, Int] = sues.find(consistentWithTape).map(_.number)
  .toRight(AocError.ValidationError("no Aunt Sue matches the ticker tape"))

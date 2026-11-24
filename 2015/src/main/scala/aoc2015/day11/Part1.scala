package aoc2015.day11

/** The next valid corporate password after `password`.
  *
  * Starting from `password`, increment repeatedly until a password satisfies all three rules at
  * once: it contains an increasing straight of three letters, avoids `i`/`o`/`l`, and contains two
  * different non-overlapping pairs. The answer is reached deterministically by counting upward.
  */
def part1(password: Password): String =
  @scala.annotation.tailrec
  def loop(candidate: Password): Password =
    if hasStraight(candidate) && hasNoAmbiguousLetters(candidate) && hasTwoPairs(candidate) then
      candidate
    else loop(increment(candidate))

  Password.show(loop(increment(password)))

end part1

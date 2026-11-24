package aoc2015.day11

/** The next valid corporate password after the one part one returns.
  *
  * Santa's password expired again, so the same rule applies a second time: starting from the
  * current password, find the next valid password, then the next valid password after that.
  */
def part2(password: Password): String = Password.show(nextPassword(nextPassword(password)))

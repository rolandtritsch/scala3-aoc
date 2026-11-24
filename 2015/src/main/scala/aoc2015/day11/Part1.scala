package aoc2015.day11

/** The next valid corporate password after `password`.
  *
  * Counting upward from `password`, this is the next password that satisfies all three rules: it
  * contains an increasing straight of three letters, avoids `i`/`o`/`l`, and contains two different
  * non-overlapping pairs.
  */
def part1(password: Password): String = Password.show(nextPassword(password))

package aoc2016.day05

def part1(doorId: DoorId): String = interestingHashes(doorId).map(sixthHexDigit)
  .take(PasswordLength).mkString

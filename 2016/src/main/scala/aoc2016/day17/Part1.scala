package aoc2016.day17

/** The shortest path to the vault as the string of moves taken, or `None` if unreachable. */
def part1(passcode: Passcode): Option[String] = shortestPath(passcode).map(pathString)

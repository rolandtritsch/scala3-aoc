package aoc2016.day13

private val Target = Coordinate(31, 39)

/** The fewest steps from the waiting area at 1,1 to the target at 31,39. */
def part1(input: Input): Option[Int] = shortestPath(input.favoriteNumber, Target)

package aoc2015.day03

/** Even instruction indices belong to Santa; odd indices belong to Robo-Santa. */
def part2(directions: Seq[Direction]): Int =
  val (santa, robo) = directions.zipWithIndex.partitionMap { (direction, index) =>
    if index % 2 == 0 then Left(direction) else Right(direction)
  }
  (visited(santa) union visited(robo)).size

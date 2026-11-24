package aoc2015.day09

/** The shortest distance of a route that visits every location exactly once.
  *
  * The distances describe a complete, undirected graph, so every ordering of the locations is a
  * valid route. The answer is the minimum total distance over all such orderings.
  */
def part1(distances: Seq[Distance]): Int =
  val lookup = routeLookup(distances)
  locations(distances).permutations.map(routeDistance(_, lookup)).min

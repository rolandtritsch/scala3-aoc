package aoc2015.day09

/** The shortest distance of a route that visits every location exactly once.
  *
  * The distances describe a complete, undirected graph, so every ordering of the locations is a
  * valid route. The answer is the minimum total distance over all such orderings.
  */
def part1(distances: Seq[Distance]): Int =
  val locations = distances.flatMap(d => Seq(d.from, d.to)).distinct
  val lookup = distances
    .flatMap(d => Seq((d.from, d.to) -> d.distance, (d.to, d.from) -> d.distance)).toMap
  locations.permutations.map(routeDistance(_, lookup)).min

/** The total distance of one route, summing the distances between consecutive locations. */
def routeDistance(route: Seq[String], lookup: Map[(String, String), Int]): Int = route
  .zip(route.tail).map((a, b) => lookup((a, b))).sum

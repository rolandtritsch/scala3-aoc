package aoc2015.day09

/** The distinct locations described by the distances, in the order they first appear. */
def locations(distances: Seq[Distance]): Seq[String] = distances.flatMap(d => Seq(d.from, d.to))
  .distinct

/** A lookup from each ordered pair of locations to the distance between them.
  *
  * Distances are undirected, so each edge is recorded under both orders of its endpoints.
  */
def routeLookup(distances: Seq[Distance]): Map[(String, String), Int] = distances
  .flatMap(d => Seq((d.from, d.to) -> d.distance, (d.to, d.from) -> d.distance)).toMap

/** The total distance of one route, summing the distances between consecutive locations. */
def routeDistance(route: Seq[String], lookup: Map[(String, String), Int]): Int = route
  .zip(route.tail).map((a, b) => lookup((a, b))).sum

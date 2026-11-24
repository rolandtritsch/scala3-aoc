package aoc2015.day09

import aoc2015.util.AocError
import aoc2015.util.parseLines
import aoc2015.util.readResource
import zio.*

/** Load and validate Day09 input for both parts.
  *
  * The payload is the parsed list of distances. The locations must form a complete graph with no
  * duplicate edges, so every ordering of the locations is a valid route.
  */
def readFile(filename: String): ZIO[Any, AocError, Seq[Distance]] = readResource(filename) {
  (name, input) =>
    parseLines(name, input)(Distance.parse).flatMap { distances =>
      val locations = distances.flatMap(d => Seq(d.from, d.to)).distinct
      val pairs = distances.map(d => if d.from < d.to then (d.from, d.to) else (d.to, d.from))
      val expected = locations.size * (locations.size - 1) / 2
      if locations.size < 2 then
        Left(AocError.ValidationError(s"$name: at least two locations are required"))
      else if pairs.distinct.size != pairs.size then
        Left(AocError.ValidationError(s"$name: duplicate distance between the same pair"))
      else if pairs.size != expected then
        Left(AocError.ValidationError(s"$name: distances do not form a complete graph"))
      else Right(distances)
    }
}

end readFile

package aoc2015.day14

/** The leading reindeer's score after `elapsed` whole seconds under the part-two scoring system.
  *
  * Part two drops the single-distance comparison: at the end of each second, every reindeer that is
  * currently tied for the lead gains one point. The result is the largest score once `elapsed`
  * seconds have run. Reindeer names are kept as the score key because `readFile` already guarantees
  * they are distinct.
  */
def pointsAfter(reindeer: Seq[Reindeer], elapsed: Int): Int =
  val finalScores = (1 to elapsed).foldLeft(Map.empty[String, Int]) { (scores, second) =>
    val distances = reindeer.map(r => r.name -> distanceAfter(r, second))
    val leading = distances.map(_._2).max
    distances.foldLeft(scores) { (next, entry) =>
      val (name, distance) = entry
      if distance == leading then next.updated(name, next.getOrElse(name, 0) + 1) else next
    }
  }
  finalScores.values.max
end pointsAfter

/** The winning reindeer's point count after the full `raceSeconds` time limit. */
def part2(reindeer: Seq[Reindeer]): Int = pointsAfter(reindeer, raceSeconds)

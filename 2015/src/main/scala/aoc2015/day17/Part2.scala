package aoc2015.day17

/** The number of ways to fill `targetLiters` liters using the fewest possible containers.
  *
  * Part two first finds the smallest number of containers whose capacities sum to the target
  * exactly, then counts how many distinct combinations use exactly that many containers. Containers
  * are distinguished by position, so two containers with the same capacity are still two different
  * combinations.
  */
def part2(containers: Seq[Liters]): Int = minimalCombinations(containers, targetLiters)

/** The number of ways to hold `target` liters using the fewest containers from `available`.
  *
  * The number of ways is grouped by how many containers each way uses; the result is the count for
  * the smallest group. An empty `available` list or an unreachable target has no ways at all.
  */
def minimalCombinations(available: Seq[Liters], target: Int): Int =
  val byCount = waysByCount(available, target)
  if byCount.isEmpty then 0 else byCount.minBy(_._1)._2

/** The number of ways to hold `target` liters using each possible number of containers.
  *
  * Each key is a container count and each value the number of distinct combinations using exactly
  * that many containers. A zero target is one way using no further containers; a negative target or
  * an empty list is no way. Otherwise the first container is either skipped or used, and the
  * remaining containers are delegated to a recursive call whose counts are shifted by one.
  */
def waysByCount(available: Seq[Liters], target: Int): Map[Int, Int] =
  if target == 0 then Map(0 -> 1)
  else if target < 0 then Map.empty
  else
    available match
      case first +: rest =>
        val skip = waysByCount(rest, target)
        val take = waysByCount(rest, target - Liters.value(first)).iterator
          .map { case (count, ways) => (count + 1) -> ways }.toMap
        mergeCounts(skip, take)
      case _ => Map.empty
  end if
end waysByCount

/** Combine two count-to-ways maps by summing the ways for each container count. */
def mergeCounts(left: Map[Int, Int], right: Map[Int, Int]): Map[Int, Int] =
  (left.keySet ++ right.keySet).iterator
    .map(count => count -> (left.getOrElse(count, 0) + right.getOrElse(count, 0))).toMap

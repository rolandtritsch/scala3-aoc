package aoc2015.day17

/** The total volume of eggnog the containers must hold, in liters. */
val targetLiters: Int = 150

/** The number of distinct container combinations that hold exactly `targetLiters` liters.
  *
  * Every subset of the containers is considered, and a combination is counted when the sum of its
  * capacities equals the target exactly. Containers are distinguished by position, so two
  * containers with the same capacity are still two different combinations.
  */
def part1(containers: Seq[Liters]): Int = countCombinations(containers, targetLiters)

/** The number of ways to fill `target` liters using only the containers in `available`.
  *
  * The first container is either used (subtracting its capacity from `target`) or skipped, and the
  * remaining containers are delegated to a recursive call. A zero target is one complete way, while
  * a negative target or an empty list is not a way at all.
  */
def countCombinations(available: Seq[Liters], target: Int): Int =
  if target == 0 then 1
  else if target < 0 then 0
  else
    available match
      case first +: rest => countCombinations(rest, target) +
          countCombinations(rest, target - Liters.value(first))
      case _ => 0
  end if
end countCombinations

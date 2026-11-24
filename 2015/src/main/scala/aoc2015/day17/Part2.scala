package aoc2015.day17

/** The minimum number of containers needed to hold exactly `target` liters. */
def minimalContainerCount(available: Seq[Liters], target: Int): Option[Int] =
  if target == 0 then Some(0)
  else if target < 0 then None
  else
    available match
      case first +: rest =>
        val useFirst = minimalContainerCount(rest, target - Liters.value(first))
        val skipFirst = minimalContainerCount(rest, target)
        (useFirst, skipFirst) match
          case (Some(u), Some(s)) => Some(1 + u min s)
          case (Some(u), None)    => Some(1 + u)
          case (None, Some(s))    => Some(s)
          case (None, None)       => None
      case _ => None
  end if
end minimalContainerCount

/** The number of distinct container combinations that hold exactly `target` liters using the fewest
  * containers possible.
  *
  * First determines the minimum number of containers needed, then counts only those combinations
  * that use exactly that many containers.
  */
def minimalCombinations(available: Seq[Liters], target: Int): Int =
  minimalContainerCount(available, target) match
    case Some(minCount) => countMinimalCombinations(available, target, minCount)
    case None           => 0
  end match

/** Count combinations using exactly `containerCount` containers that sum to `target`. */
private def countMinimalCombinations(
    available: Seq[Liters],
    target: Int,
    containerCount: Int,
): Int =
  if target == 0 && containerCount == 0 then 1
  else if target <= 0 || containerCount <= 0 then 0
  else
    available match
      case first +: rest =>
        val useFirst =
          countMinimalCombinations(rest, target - Liters.value(first), containerCount - 1)
        val skipFirst = countMinimalCombinations(rest, target, containerCount)
        useFirst + skipFirst
      case _ => 0
    end match
  end if
end countMinimalCombinations

/** The number of ways to fill exactly `targetLiters` using the fewest containers possible. */
def part2(containers: Seq[Liters]): Int = minimalCombinations(containers, targetLiters)

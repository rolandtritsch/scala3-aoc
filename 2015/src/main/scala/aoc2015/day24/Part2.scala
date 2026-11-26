package aoc2015.day24

/** Quantum entanglement of the ideal first group for four groups. */
def part2(weights: Seq[Int]): Long =
  val total = weights.sum
  require(total % 4 == 0, s"Total weight $total not divisible by 4")
  val target = total / 4

  val sortedWeights = weights.sorted(using Ordering.Int.reverse)

  @scala.annotation.tailrec
  def findMinQE(groupSize: Int): Long =
    val combinations = findCombinations(sortedWeights, groupSize, target)
    val validCombinations = combinations
      .filter(combo => canPartition(weights.diff(combo), target, 3))
    if validCombinations.nonEmpty then validCombinations.map(_.map(_.toLong).product).min
    else findMinQE(groupSize + 1)
  end findMinQE

  findMinQE(1)
end part2

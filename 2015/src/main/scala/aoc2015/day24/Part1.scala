package aoc2015.day24

/** Quantum entanglement of the ideal first group (minimum packages, then minimum QE). */
def part1(weights: Seq[Int]): Long =
  val total = weights.sum
  require(total % 3 == 0, s"Total weight $total not divisible by 3")
  val target = total / 3

  val sortedWeights = weights.sorted(using Ordering.Int.reverse)

  @scala.annotation.tailrec
  def findMinQE(groupSize: Int): Long =
    val combinations = findCombinations(sortedWeights, groupSize, target)
    val validCombinations = combinations
      .filter(combo => canPartition(weights.diff(combo), target, 2))
    if validCombinations.nonEmpty then validCombinations.map(_.map(_.toLong).product).min
    else findMinQE(groupSize + 1)
  end findMinQE

  findMinQE(1)
end part1

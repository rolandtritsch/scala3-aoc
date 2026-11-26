package aoc2015.day20

/** Placeholder for Part 2 solution.
  *
  * Part 2: Each elf now only delivers to the first 50 houses. So elf n delivers to houses n, 2n,
  * 3n, ..., 50n. House k receives 11 * (sum of divisors d of k where k/d <= 50) presents.
  */
def part2(input: Input): Int =
  val target = input.target
  val upperBound = findUpperBoundPart2(target)
  val divisorSums = computeLimitedDivisorSums(upperBound, 50)

  divisorSums.zipWithIndex.drop(1).find(_._1 * 11 >= target).map(_._2).getOrElse {
    val extendedBound = upperBound * 2
    val extendedSums = computeLimitedDivisorSums(extendedBound, 50)
    extendedSums.zipWithIndex.drop(1).find(_._1 * 11 >= target).map(_._2).get
  }
end part2

/** Compute sum of divisors d for each n where n/d <= limit (i.e., d >= n/limit). */
private def computeLimitedDivisorSums(n: Int, limit: Int): Array[Int] =
  val sums = Array.fill(n + 1)(0)
  for i <- 1 to n do
    // Elf i visits houses i, 2i, ..., limit*i
    val maxHouse = math.min(n, i * limit)
    for j <- i to maxHouse by i do sums(j) += i
  sums
end computeLimitedDivisorSums

private def findUpperBoundPart2(target: Int): Int =
  // For part 2, each elf delivers 11 presents instead of 10, but only to 50 houses
  // The sum is more limited, so we need a larger bound
  math.max(100, target / 11)

package aoc2015.day20

/** Finds the lowest house number that receives at least the target number of presents.
  *
  * Each elf n delivers n * 10 presents to every house that is a multiple of n. So house k receives
  * 10 * (sum of divisors of k) presents. We need the smallest k such that 10 * sumOfDivisors(k) >=
  * target.
  */
def part1(input: Input): Int =
  val target = input.target
  val targetDivisorSum = target / 10

  // Use a sieve to compute sum of divisors for all numbers up to an upper bound
  // The sum of divisors of n is at most n * (1 + 1/2 + ... + 1/n) ~ n * log n
  // But typically much smaller. We'll use a bound and extend if needed.
  val upperBound = findUpperBound(targetDivisorSum)
  val divisorSums = computeDivisorSums(upperBound)

  divisorSums.zipWithIndex.drop(1) // house numbers start at 1
    .find(_._1 >= targetDivisorSum).map(_._2).getOrElse {
      // If not found, extend the search
      val extendedBound = upperBound * 2
      val extendedSums = computeDivisorSums(extendedBound)
      extendedSums.zipWithIndex.drop(1).find(_._1 >= targetDivisorSum).map(_._2).get
    }
end part1

/** Compute sum of divisors for all numbers from 0 to n using a sieve. */
private def computeDivisorSums(n: Int): Array[Int] =
  val sums = Array.fill(n + 1)(0)
  for i <- 1 to n do for j <- i to n by i do sums(j) += i
  sums

/** Find an upper bound for the search. */
private def findUpperBound(targetDivisorSum: Int): Int =
  // The sum of divisors of n is at least n + 1 (for n > 1)
  // And at most roughly n * log(n) for large n
  // For targetDivisorSum, a reasonable starting bound is targetDivisorSum
  // but we can be smarter: since sum of divisors grows roughly like n,
  // we can start with something like targetDivisorSum / 2 or similar
  // Actually, for abundant numbers, sum of divisors can be > 2n
  // Let's use a safe overestimate
  math.max(100, targetDivisorSum / 2)
end findUpperBound

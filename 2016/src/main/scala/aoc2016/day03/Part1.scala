package aoc2016.day03

def part1(input: Seq[Triangle]): Int = input.count(isPossible)

private def isPossible(triangle: Triangle): Boolean =
  val a = triangle.first.toLong
  val b = triangle.second.toLong
  val c = triangle.third.toLong
  a + b > c && b + c > a && c + a > b

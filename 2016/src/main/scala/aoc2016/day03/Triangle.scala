package aoc2016.day03

final case class Triangle(first: Int, second: Int, third: Int)

def isPossible(triangle: Triangle): Boolean =
  val a = triangle.first.toLong
  val b = triangle.second.toLong
  val c = triangle.third.toLong
  a + b > c && b + c > a && c + a > b

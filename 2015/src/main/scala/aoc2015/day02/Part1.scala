package aoc2015.day02

def wrappingPaper(present: Present): BigInt =
  val Present(l, w, h) = present
  val sides = Seq(l * w, w * h, h * l)
  2 * sides.sum + sides.min // Exactly three sides, so min is always defined.

def part1(presents: Seq[Present]): BigInt = presents.foldLeft(BigInt(0))(_ + wrappingPaper(_))

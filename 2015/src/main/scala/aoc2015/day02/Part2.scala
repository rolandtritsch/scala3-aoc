package aoc2015.day02

def ribbon(present: Present): BigInt =
  val Present(l, w, h) = present
  2 * (l + w + h - l.max(w).max(h)) + l * w * h

def part2(presents: Seq[Present]): BigInt = presents.foldLeft(BigInt(0))(_ + ribbon(_))

package aoc2016.day20

/** The part-one answer: the lowest-valued IP that no blocked range covers.
  *
  * Sweeping the ranges in ascending start order, `coveredThrough` is the highest IP known to be
  * blocked so far. A range starting above `coveredThrough + 1` leaves a gap, and that gap's first
  * IP is the answer. A range overlapping or extending the covered region pushes the frontier to its
  * end. If the frontier reaches `4294967295` with no gap, every IP is blocked and the answer is
  * `-1`.
  */
def part1(input: Input): Long =
  val sorted = input.ranges.sortBy(range => (range.start, range.end))
  val (answer, frontier) = sorted.foldLeft((-1L, -1L)) { case ((answer, coveredThrough), range) =>
    if answer >= 0L then (answer, coveredThrough)
    else if range.start > coveredThrough + 1L then (coveredThrough + 1L, coveredThrough)
    else (answer, math.max(coveredThrough, range.end))
  }
  if answer >= 0L then answer else if frontier >= 0xffffffffL then -1L else frontier + 1L
end part1

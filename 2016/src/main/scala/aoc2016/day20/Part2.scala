package aoc2016.day20

/** The part-two answer: how many 32-bit IPs no blocked range covers.
  *
  * Sweeping the ranges in ascending start order, `coveredThrough` is the highest IP known to be
  * blocked so far. A range starting above `coveredThrough + 1` leaves a gap of allowed IPs whose
  * size is `range.start - coveredThrough - 1`, added to the running count. Overlapping or extending
  * ranges push the frontier to their end. After the sweep, every IP above the frontier up to
  * `4294967295` is also allowed. The count fits in a `Long` because there are `4294967296` IPs.
  */
def part2(input: Input): Long =
  val sorted = input.ranges.sortBy(range => (range.start, range.end))
  val (allowed, coveredThrough) = sorted
    .foldLeft((0L, -1L)) { case ((allowed, coveredThrough), range) =>
      if range.start > coveredThrough + 1L then
        (allowed + (range.start - coveredThrough - 1L), math.max(coveredThrough, range.end))
      else (allowed, math.max(coveredThrough, range.end))
    }
  allowed + (0xffffffffL - coveredThrough)
end part2

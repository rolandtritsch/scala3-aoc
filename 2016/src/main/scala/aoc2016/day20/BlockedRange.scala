package aoc2016.day20

/** A closed, inclusive range of blocked 32-bit IP addresses.
  *
  * `start` and `end` are already validated so that `0 <= start <= end`.
  */
final case class BlockedRange(start: Long, end: Long):
  require(start >= 0L && end <= 0xffffffffL && start <= end)

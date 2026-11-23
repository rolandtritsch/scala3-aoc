package aoc2015.day05

/** Remember each pair's earliest index so overlapping occurrences cannot hide a later valid match.
  * A Map provides efficient lookup by pair instead of comparing every pair of positions.
  */
def hasRepeatingPair(s: String): Boolean =
  @scala.annotation.tailrec
  def search(index: Int, firstSeen: Map[String, Int]): Boolean =
    if index >= s.length - 1 then false
    else
      val pair = s.substring(index, index + 2)
      firstSeen.get(pair) match
        case Some(first) if index - first >= 2 => true
        case Some(_)                           => search(index + 1, firstSeen)
        case None                              => search(index + 1, firstSeen.updated(pair, index))

  search(0, Map.empty)

end hasRepeatingPair

/** @return
  *   true if `s` contains at least one letter which repeats with exactly one letter between them
  *   (i.e. a three-character sliding window where the first and last characters are equal).
  */
def hasRepeatWithGap(s: String): Boolean = s.sliding(3)
  .exists(triple => triple.length == 3 && triple(0) == triple(2))
end hasRepeatWithGap

/** @return true if `s` satisfies both part-2 niceness rules. */
def isNicePart2(s: String): Boolean = hasRepeatingPair(s) && hasRepeatWithGap(s)
end isNicePart2

/** Count words satisfying both revised rules. Pair lookups avoid a quadratic pair comparison. */
def part2(strings: Seq[String]): Int = strings.count(isNicePart2)

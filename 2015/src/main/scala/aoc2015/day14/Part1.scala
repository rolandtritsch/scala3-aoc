package aoc2015.day14

/** The number of seconds the part-one race lasts before the winning distance is measured. */
val raceSeconds: Int = 2503

/** The distance a reindeer covers after a whole number of elapsed seconds.
  *
  * Every reindeer repeats a fixed cycle: it flies at `speed` for `flySeconds`, rests for
  * `restSeconds`, then starts over. The distance after `elapsed` seconds is `speed` times the
  * number of seconds spent flying, which is one full `flySeconds` block per completed cycle plus
  * the flying portion of the final, partial cycle.
  */
def distanceAfter(reindeer: Reindeer, elapsed: Int): Int =
  val cycle = reindeer.flySeconds + reindeer.restSeconds
  val completedCycles = elapsed / cycle
  val remainder = elapsed % cycle
  val finalFlySeconds = remainder.min(reindeer.flySeconds)
  reindeer.speed * (completedCycles * reindeer.flySeconds + finalFlySeconds)
end distanceAfter

/** The furthest distance travelled by any reindeer after `raceSeconds`. */
def part1(reindeer: Seq[Reindeer]): Int = reindeer.map(distanceAfter(_, raceSeconds)).max

package aoc2016.day15

import scala.annotation.tailrec

/** Whether a capsule dropped at `time` passes through every disc's slot.
  *
  * A capsule reaches disc `d` at `time + d.number`, when that disc has ticked `time + d.number`
  * positions from its start; the slot is at position 0.
  */
private def passes(discs: Seq[Disc], time: Int): Boolean = discs
  .forall(disc => (disc.start + time + disc.number) % disc.positions == 0)

/** The first button time at which a capsule falls through all the discs. */
def part1(discs: Seq[Disc]): Int =
  @tailrec
  def search(time: Int): Int = if passes(discs, time) then time else search(time + 1)

  search(0)

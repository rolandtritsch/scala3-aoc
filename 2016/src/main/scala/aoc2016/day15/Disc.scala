package aoc2016.day15

/** A rotating disc: its number of stopping positions and the slot it occupies at time 0.
  *
  * The `number` is the disc's ordinal in the sculpture; a capsule dropped at time `t` reaches this
  * disc at time `t + number`.
  */
final case class Disc(number: Int, positions: Int, start: Int)

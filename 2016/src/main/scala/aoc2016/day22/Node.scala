package aoc2016.day22

/** One storage node at grid position `(x, y)` with its total size, used space, and available space.
  *
  * Capacities are kept as `Long` because the puzzle reports terabyte values that may exceed `Int`
  * range on other inputs.
  */
final case class Node(x: Int, y: Int, size: Long, used: Long, avail: Long):
  /** A node is empty exactly when none of its space is used. */
  def isEmpty: Boolean = used == 0L
end Node

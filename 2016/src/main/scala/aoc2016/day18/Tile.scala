package aoc2016.day18

/** A single floor tile: either safe to step on or a trap. */
enum Tile:
  case Safe
  case Trap

  /** Whether this tile is safe to step on. */
  def isSafe: Boolean = this == Tile.Safe

end Tile

/** Parse one tile character, or `None` for any other character. */
def tileFromChar(c: Char): Option[Tile] = PartialFunction.condOpt(c):
  case '.' => Tile.Safe
  case '^' => Tile.Trap

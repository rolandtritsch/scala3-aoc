package aoc2016.day17

/** A room in the 4x4 vault grid, with (0, 0) at the top-left start and (3, 3) at the vault. */
final case class Position(x: Int, y: Int)

private[day17] val Start: Position = Position(0, 0)
private[day17] val Vault: Position = Position(3, 3)

/** The room reached by taking `move` from `position`. */
private[day17] def moveTo(position: Position, move: Move): Position =
  Position(position.x + move.dx, position.y + move.dy)

/** Whether a room lies within the 4x4 grid. */
private[day17] def isInside(position: Position): Boolean = position.x >= 0 && position.x <= 3 &&
  position.y >= 0 && position.y <= 3

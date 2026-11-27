package aoc2016.day13

/** A non-negative location in the infinite cubicle maze. */
final case class Coordinate(x: Int, y: Int)

/** The open/wall rule is pure: a cell is open exactly when the bit count of the designer's formula
  * is even.
  */
def isOpen(favoriteNumber: Int, coordinate: Coordinate): Boolean =
  val x = coordinate.x.toLong
  val y = coordinate.y.toLong
  val value = x * x + 3 * x + 2 * x * y + y + y * y + favoriteNumber
  java.lang.Long.bitCount(value) % 2 == 0

/** The four orthogonal neighbours, discarding coordinates that leave the building. */
def neighbours(coordinate: Coordinate): Seq[Coordinate] = Seq(
  Coordinate(coordinate.x - 1, coordinate.y),
  Coordinate(coordinate.x + 1, coordinate.y),
  Coordinate(coordinate.x, coordinate.y - 1),
  Coordinate(coordinate.x, coordinate.y + 1),
).filter(cell => cell.x >= 0 && cell.y >= 0)
end neighbours

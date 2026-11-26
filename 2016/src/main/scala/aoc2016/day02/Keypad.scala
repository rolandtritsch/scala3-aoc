package aoc2016.day02

private val Keypad = Seq(
  Seq(Button.One, Button.Two, Button.Three),
  Seq(Button.Four, Button.Five, Button.Six),
  Seq(Button.Seven, Button.Eight, Button.Nine),
)

private def move(button: Button, direction: Direction): Button =
  val (rowOffset, columnOffset) = direction match
    case Direction.Up    => (-1, 0)
    case Direction.Down  => (1, 0)
    case Direction.Left  => (0, -1)
    case Direction.Right => (0, 1)
  val row = button.row + rowOffset
  val column = button.column + columnOffset
  Keypad.lift(row).flatMap(_.lift(column)).getOrElse(button)
end move

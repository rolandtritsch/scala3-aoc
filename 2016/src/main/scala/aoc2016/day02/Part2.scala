package aoc2016.day02

private enum DiamondButton(val label: Char, val row: Int, val column: Int):
  case One extends DiamondButton('1', 0, 2)
  case Two extends DiamondButton('2', 1, 1)
  case Three extends DiamondButton('3', 1, 2)
  case Four extends DiamondButton('4', 1, 3)
  case Five extends DiamondButton('5', 2, 0)
  case Six extends DiamondButton('6', 2, 1)
  case Seven extends DiamondButton('7', 2, 2)
  case Eight extends DiamondButton('8', 2, 3)
  case Nine extends DiamondButton('9', 2, 4)
  case A extends DiamondButton('A', 3, 1)
  case B extends DiamondButton('B', 3, 2)
  case C extends DiamondButton('C', 3, 3)
  case D extends DiamondButton('D', 4, 2)

private val DiamondKeypad = Seq(
  Seq(None, None, Some(DiamondButton.One), None, None),
  Seq(None, Some(DiamondButton.Two), Some(DiamondButton.Three), Some(DiamondButton.Four), None),
  Seq(
    Some(DiamondButton.Five),
    Some(DiamondButton.Six),
    Some(DiamondButton.Seven),
    Some(DiamondButton.Eight),
    Some(DiamondButton.Nine),
  ),
  Seq(None, Some(DiamondButton.A), Some(DiamondButton.B), Some(DiamondButton.C), None),
  Seq(None, None, Some(DiamondButton.D), None, None),
)

private def moveDiamond(button: DiamondButton, direction: Direction): DiamondButton =
  val (rowOffset, columnOffset) = direction match
    case Direction.Up    => (-1, 0)
    case Direction.Down  => (1, 0)
    case Direction.Left  => (0, -1)
    case Direction.Right => (0, 1)
  val row = button.row + rowOffset
  val column = button.column + columnOffset
  DiamondKeypad.lift(row).flatMap(_.lift(column)).flatten.getOrElse(button)
end moveDiamond

def part2(input: Seq[Seq[Direction]]): String = input
  .scanLeft(DiamondButton.Five)((button, instructions) =>
    instructions.foldLeft(button)(moveDiamond)
  ).drop(1).map(_.label).mkString

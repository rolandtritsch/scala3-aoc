package aoc2016.day08

// A set represents exactly the lit pixels, so overlapping rectangles and rotations cannot double-count them.
private[day08] final case class Screen(width: Int, height: Int, lit: Set[Pixel])

private[day08] def runScreen(width: Int, height: Int, instructions: Seq[Instruction]): Screen =
  instructions.foldLeft(Screen(width, height, Set.empty))(applyInstruction)

private def applyInstruction(screen: Screen, instruction: Instruction): Screen = instruction match
  case Instruction.Rect(width, height) =>
    val rectangle =
      for
        y <- 0 until height
        x <- 0 until width
      yield Pixel(x, y)
    screen.copy(lit = screen.lit ++ rectangle)
  case Instruction.RotateRow(row, amount) =>
    val shift = amount % screen.width
    screen.copy(lit = screen.lit.map { pixel =>
      if pixel.y == row then pixel.copy(x = (pixel.x + shift) % screen.width) else pixel
    })
  case Instruction.RotateColumn(column, amount) =>
    val shift = amount % screen.height
    screen.copy(lit = screen.lit.map { pixel =>
      if pixel.x == column then pixel.copy(y = (pixel.y + shift) % screen.height) else pixel
    })
end applyInstruction

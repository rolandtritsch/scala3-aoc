package aoc2015.day18

/** Simulate 100 steps of Conway's Game of Life on a 100x100 grid with four corners stuck on and
  * return the number of lights that are on.
  */
def part2(initialGrid: Seq[Seq[Light]]): Int =
  val steps = 100
  val gridWithCornersOn = initialGrid.map(_.toArray).toArray
  setCornersOn(gridWithCornersOn)
  val finalGrid = (1 to steps).foldLeft(gridWithCornersOn) { (grid, _) =>
    val nextGrid = Array.ofDim[Light](gridSide, gridSide)
    for y <- 0 until gridSide do
      for x <- 0 until gridSide do
        if isCorner(x, y) then nextGrid(y)(x) = Light(true)
        else
          val neighborsOn = countNeighbors(grid, x, y)
          val isOn = Light.value(grid(y)(x))
          nextGrid(y)(x) = nextState(isOn, neighborsOn)
    end for
    setCornersOn(nextGrid)
    nextGrid
  }
  finalGrid.flatten.count(Light.value)
end part2

/** Check if (x, y) is one of the four corners. */
private def isCorner(x: Int, y: Int): Boolean =
  (x == 0 || x == gridSide - 1) && (y == 0 || y == gridSide - 1)

/** Set the four corners to on. */
private def setCornersOn(grid: Array[Array[Light]]): Unit =
  grid(0)(0) = Light(true)
  grid(0)(gridSide - 1) = Light(true)
  grid(gridSide - 1)(0) = Light(true)
  grid(gridSide - 1)(gridSide - 1) = Light(true)

package aoc2015.day18

/** Simulate 100 steps of Conway's Game of Life on a 100x100 grid and return the number of lights
  * that are on.
  */
def part1(initialGrid: Seq[Seq[Light]]): Int =
  val steps = 100
  val finalGrid = (1 to steps).foldLeft(initialGrid.map(_.toArray).toArray) { (grid, _) =>
    val nextGrid = Array.ofDim[Light](gridSide, gridSide)
    for y <- 0 until gridSide do
      for x <- 0 until gridSide do
        val neighborsOn = countNeighbors(grid, x, y)
        val isOn = Light.value(grid(y)(x))
        nextGrid(y)(x) = nextState(isOn, neighborsOn)
    end for
    nextGrid
  }
  finalGrid.flatten.count(Light.value)
end part1

/** Count the number of neighbors that are on for a cell at (x, y). */
private def countNeighbors(grid: Array[Array[Light]], x: Int, y: Int): Int = (for
  dy <- -1 to 1
  dx <- -1 to 1 if dx != 0 || dy != 0
  nx = x + dx
  ny = y + dy if nx >= 0 && nx < gridSide && ny >= 0 && ny < gridSide
  if Light.value(grid(ny)(nx))
yield 1).length
end countNeighbors

/** Determine the next state of a cell based on its current state and number of on neighbors. */
private def nextState(isOn: Boolean, neighborsOn: Int): Light =
  if isOn then
    // A light which is on stays on when 2 or 3 neighbors are on, and turns off otherwise.
    Light(neighborsOn == 2 || neighborsOn == 3)
  else
    // A light which is off turns on if exactly 3 neighbors are on, and stays off otherwise.
    Light(neighborsOn == 3)
end nextState

package aoc2015.day18

import aoc2015.util.AocError
import zio.test.*

object Day18Test extends ZIOSpecDefault:

  def spec: Spec[Any, Any] = suite("Day18")(
    test("Light.parseLine - parses a valid 100-char line"):
      val line = "." * 50 + "#" * 50
      assertTrue(Light.parseLine(line, 1).map(_.length) == Right(100))
    ,
    test("Light.parseLine - rejects invalid characters"):
      val line = "." * 99 + "x"
      assertTrue(Light.parseLine(line, 1).isLeft)
    ,
    test("Light.parseLine - rejects wrong length"):
      assertTrue(Light.parseLine(".#.#.", 1).isLeft)
    ,
    test("part1 - 6x6 example after 4 steps has 4 lights on"):
      val input = Seq(".#.#.#", "...##.", "#....#", "..#...", "#.#..#", "####..")
      val grid = input.map(parseTestLine)
      val result = simulate(grid, 4, 6)
      assertTrue(Light.countOn(result) == 4)
    ,
    test("part1 - 6x6 example after 1 step"):
      val input = Seq(".#.#.#", "...##.", "#....#", "..#...", "#.#..#", "####..")
      val grid = input.map(parseTestLine)
      val result = simulate(grid, 1, 6)
      val expected = Seq("..##..", "..##.#", "...##.", "......", "#.....", "#.##..")
      val expectedGrid = expected.map(parseTestLine)
      assertTrue(result == expectedGrid)
    ,
    test("readFile - loads real input"):
      readFile("inputs/Day18.txt")
        .map(grid => assertTrue(grid.length == 100 && grid.head.length == 100))
    ,
    test("readFile - malformed line reports error"):
      readFile("inputs/Day18Invalid.txt").either.map(result =>
        assertTrue(result.left.exists {
          case AocError.ParseError("inputs/Day18Invalid.txt", message) => message.contains("line 2")
          case _                                                       => false
        })
      )
    ,
    test("part1 - real input answer"):
      readFile("inputs/Day18.txt").map(grid => assertTrue(part1(grid) == 821)),
  )
  end spec

  /** Parse a test line without length validation. */
  private def parseTestLine(line: String): Seq[Light] = line.map {
    case '#' => Light(true)
    case '.' => Light(false)
    case c   => sys.error(s"Invalid character in test line: $c")
  }

  /** Simulate Conway's Game of Life for a given number of steps on a grid of given size. */
  private def simulate(grid: Seq[Seq[Light]], steps: Int, size: Int): Seq[Seq[Light]] = (1 to steps)
    .foldLeft(grid.map(_.toArray).toArray) { (currentGrid, _) =>
      val nextGrid = Array.ofDim[Light](size, size)
      for y <- 0 until size do
        for x <- 0 until size do
          val neighborsOn = countNeighborsTest(currentGrid, x, y, size)
          val isOn = Light.value(currentGrid(y)(x))
          nextGrid(y)(x) = nextState(isOn, neighborsOn)
      end for
      nextGrid
    }.map(_.toSeq).toSeq
  end simulate

  /** Count neighbors for test simulation. */
  private def countNeighborsTest(grid: Array[Array[Light]], x: Int, y: Int, size: Int): Int = (for
    dy <- -1 to 1
    dx <- -1 to 1 if dx != 0 || dy != 0
    nx = x + dx
    ny = y + dy if nx >= 0 && nx < size && ny >= 0 && ny < size
    if Light.value(grid(ny)(nx))
  yield 1).length
  end countNeighborsTest

  /** Next state for test simulation. */
  private def nextState(isOn: Boolean, neighborsOn: Int): Light =
    if isOn then Light(neighborsOn == 2 || neighborsOn == 3) else Light(neighborsOn == 3)

end Day18Test

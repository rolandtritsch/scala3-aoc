package aoc2016.day13

import zio.test.*

object Day13Test extends ZIOSpecDefault:

  private val officialGrid = """.#.####.##
                               |..#..#...#
                               |#....##...
                               |###.#.###.
                               |.##..#..#.
                               |..##....#.
                               |#...##.###""".stripMargin

  def spec: Spec[Any, Any] = suite("Day13")(
    test("parse - accepts a single non-negative favorite number and ignores blank lines"):
      assertTrue(
        parse("sample", " 10 \n") == Right(Input(10)),
        parse("sample", "1362\n\n") == Right(Input(1362)),
      )
    ,
    test("parse - rejects empty, multiple, negative, malformed and overflowing input"):
      assertTrue(
        Seq("", "\n", "10\n20", "-1", "abc", "2147483648")
          .forall(input => parse("sample", input).isLeft)
      )
    ,
    test("isOpen - matches the documented example layout"):
      val open = (for
        y <- 0 until 7
        x <- 0 until 10 if isOpen(10, Coordinate(x, y))
      yield (x, y)).toSet
      val documented = (for
        (row, y) <- officialGrid.linesIterator.zipWithIndex
        (cell, x) <- row.zipWithIndex if cell == '.'
      yield (x, y)).toSet
      assertTrue(open == documented)
    ,
    test("part1 - official example reaches 7,4 in eleven steps"):
      assertTrue(shortestPath(10, Coordinate(7, 4)) == Some(11))
    ,
    test("part1 - the start is the target in zero steps"):
      assertTrue(shortestPath(10, Start) == Some(0))
    ,
    test("readFile and part1 - personal input"):
      readFile("inputs/Day13.txt").map(input => assertTrue(part1(input) == Some(82))),
  )
  end spec

end Day13Test

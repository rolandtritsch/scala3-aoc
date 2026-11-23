package aoc2015

import aoc2015.Day03.*
import zio.*
import zio.test.*

object Day03Test extends ZIOSpecDefault:

  def spec: Spec[Any, Any] = suite("Day03")(
    test("part1 - official samples"):
      assertTrue(part1(">") == 2, part1("^>v<") == 4, part1("^v^v^v^v^v") == 2)
    ,
    test("part1 - empty input"):
      assertTrue(part1("") == 1)
    ,
    test("part1 - single moves"):
      assertTrue(part1("^") == 2, part1("v") == 2, part1("<") == 2)
    ,
    test("part1 - back and forth"):
      assertTrue(part1("<>") == 2, part1("^v") == 2)
    ,
    test("part1 - square"):
      assertTrue(part1("^>v<") == 4)
    ,
    test("part1 - real input"):
      readFile("inputs/Day03.txt").map(input => assertTrue(part1(input) == 2592))
    ,
    test("part2 - official samples"):
      assertTrue(part2("^v") == 3, part2("^>v<") == 3, part2("^v^v^v^v^v") == 11)
    ,
    test("part2 - empty input"):
      assertTrue(part2("") == 1)
    ,
    test("part2 - single moves"):
      assertTrue(part2("^") == 2, part2("v") == 2, part2(">") == 2, part2("<") == 2)
    ,
    test("part2 - directions split across the two movers"):
      assertTrue(part2("<>") == 3, part2("^v") == 3)
    ,
    test("part2 - real input"):
      readFile("inputs/Day03.txt").map(input => assertTrue(part2(input) == 2360)),
  )

end Day03Test

package aoc2015.day22

import zio.Console
import zio.test.*

object Day22Test extends ZIOSpecDefault:

  def spec: Spec[Any, Any] = suite("Day22")(
    test("part1 - example 1 from problem"):
      val input = Input(13, 8)
      assertTrue(part1(input) > 0)
    ,
    test("part1 - example 2 from problem"):
      val input = Input(14, 8)
      assertTrue(part1(input) > 0)
    ,
    test("readFile - loads real input"):
      readFile("inputs/Day22.txt")
        .map(input => assertTrue(input.hitPoints == 51, input.damage == 9))
    ,
    test("part1 - real input answer"):
      readFile("inputs/Day22.txt").flatMap { input =>
        val ans = part1(input)
        Console.printLine(s"Day22 Part1 answer: $ans").as(assertTrue(ans > 0))
      },
  )
  end spec

end Day22Test

package aoc2015.day22

import zio.Console
import zio.test.*

object Day22Test extends ZIOSpecDefault:

  def spec: Spec[Any, Any] = suite("Day22")(
    test("part1 - trivial boss 1 HP"):
      val input = Input(1, 1)
      val ans = part1(input)
      Console.printLine(s"Part1 trivial: $ans").as(assertTrue(ans == 53))
    ,
    test("part2 - trivial boss 1 HP (hard mode)"):
      val input = Input(1, 1)
      val ans = part2(input)
      Console.printLine(s"Part2 trivial: $ans").as(assertTrue(ans == 53))
    ,
    test("part1 - example 1 from problem"):
      val input = Input(13, 8)
      val ans = part1(input)
      Console.printLine(s"Part1 example 1: $ans").as(assertTrue(ans > 0))
    ,
    test("part1 - example 2 from problem"):
      val input = Input(14, 8)
      val ans = part1(input)
      Console.printLine(s"Part1 example 2: $ans").as(assertTrue(ans > 0))
    ,
    test("part2 - example 1 from problem (hard mode)"):
      val input = Input(13, 8)
      val ans = part2(input)
      Console.printLine(s"Part2 example 1: $ans").as(assertTrue(ans > 0))
    ,
    test("part2 - example 2 from problem (hard mode)"):
      val input = Input(14, 8)
      val ans = part2(input)
      Console.printLine(s"Part2 example 2: $ans").as(assertTrue(ans > 0))
    ,
    test("readFile - loads real input"):
      readFile("inputs/Day22.txt")
        .map(input => assertTrue(input.hitPoints == 51, input.damage == 9))
    ,
    test("part1 - real input answer"):
      readFile("inputs/Day22.txt").flatMap { input =>
        val ans = part1(input)
        Console.printLine(s"Day22 Part1 answer: $ans").as(assertTrue(ans == 900))
      }
    ,
    test("part2 - real input answer"):
      readFile("inputs/Day22.txt").flatMap { input =>
        val ans = part2(input)
        Console.printLine(s"Day22 Part2 answer: $ans").as(assertTrue(ans > 0))
      },
  )
  end spec

end Day22Test

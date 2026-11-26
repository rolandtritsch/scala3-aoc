package aoc2015.day24

import aoc2015.util.AocError
import zio.test.*

object Day24Test extends ZIOSpecDefault:

  def spec: Spec[Any, Any] = suite("Day24")(
    test("parse - official example"):
      val input = """1
2
3
4
5
7
8
9
10
11"""
      assertTrue(parse("sample", input) == Right(Seq(1, 2, 3, 4, 5, 7, 8, 9, 10, 11)))
    ,
    test("parse - puzzle input"):
      assertTrue(parse("sample", "1\n3\n5\n").map(_.take(3)) == Right(Seq(1, 3, 5)))
    ,
    test("readFile - typed weights"):
      readFile("inputs/Day24.txt")
        .map(weights => assertTrue(weights.size == 28, weights.head == 1, weights.last == 113))
    ,
    test("readFile - missing resource"):
      readFile("inputs/Day24-missing.txt").either.map(result =>
        assertTrue(result.left.exists {
          case AocError.FileReadError("inputs/Day24-missing.txt", _) => true
          case _                                                     => false
        })
      )
    ,
    test("part1 - official example"):
      val input = Seq(1, 2, 3, 4, 5, 7, 8, 9, 10, 11)
      assertTrue(part1(input) == 99)
    ,
    test("part1 - real input"):
      readFile("inputs/Day24.txt").map(input => assertTrue(part1(input) == 11266889531L))
    ,
    test("part2 - official example"):
      val input = Seq(1, 2, 3, 4, 5, 7, 8, 9, 10, 11)
      assertTrue(part2(input) == 44)
    ,
    test("part2 - real input"):
      readFile("inputs/Day24.txt").map(input => assertTrue(part2(input) == 77387711L)),
  )
  end spec

end Day24Test

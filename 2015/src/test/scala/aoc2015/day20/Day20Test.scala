package aoc2015.day20

import zio.test.*

object Day20Test extends ZIOSpecDefault:

  def spec: Spec[Any, Any] = suite("Day20")(
    test("part1 - example from problem (first 9 houses)"):
      // Test the divisor sum computation manually
      val sums = computeDivisorSums(9)
      assertTrue(
        sums(1) == 1, // 1
        sums(2) == 3, // 1+2
        sums(3) == 4, // 1+3
        sums(4) == 7, // 1+2+4
        sums(5) == 6, // 1+5
        sums(6) == 12, // 1+2+3+6
        sums(7) == 8, // 1+7
        sums(8) == 15, // 1+2+4+8
        sums(9) == 13, // 1+3+9
      )
    ,
    test("part1 - example presents calculation"):
      // House 1: 10*1 = 10
      // House 2: 10*(1+2) = 30
      // House 3: 10*(1+3) = 40
      // House 4: 10*(1+2+4) = 70
      // House 5: 10*(1+5) = 60
      // House 6: 10*(1+2+3+6) = 120
      // House 7: 10*(1+7) = 80
      // House 8: 10*(1+2+4+8) = 150
      // House 9: 10*(1+3+9) = 130
      val sums = computeDivisorSums(9)
      assertTrue(
        sums(1) * 10 == 10,
        sums(2) * 10 == 30,
        sums(3) * 10 == 40,
        sums(4) * 10 == 70,
        sums(5) * 10 == 60,
        sums(6) * 10 == 120,
        sums(7) * 10 == 80,
        sums(8) * 10 == 150,
        sums(9) * 10 == 130,
      )
    ,
    test("part1 - find house for small target"):
      // Target 70 -> house 4 (70 presents)
      val input = Input(70)
      assertTrue(part1(input) == 4)
    ,
    test("part1 - target 120 -> house 6"):
      val input = Input(120)
      assertTrue(part1(input) == 6)
    ,
    test("part1 - target 130 -> house 6 (since house 6 has 120, house 8 has 150)"):
      // Wait, house 6 has 120, house 7 has 80, house 8 has 150
      // So target 130 -> house 8
      val input = Input(130)
      assertTrue(part1(input) == 8)
    ,
    test("readFile - loads real input"):
      readFile("inputs/Day20.txt").map(input => assertTrue(input.target == 36000000))
    ,
    test("part1 - real input answer"):
      readFile("inputs/Day20.txt").map(input => assertTrue(part1(input) == 831600)),
  )
  end spec

end Day20Test

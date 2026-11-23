package aoc2015

import aoc2015.Day00.*
import zio.*
import zio.test.*

object Day00Test extends ZIOSpecDefault:

  private val part2Answer = BigInt(
    "43466557686937456435688527675040625802564660517371780402481729089536555417949051890403879840079255169295922593080322634775209689623239873322471161642996440906533187938298969649928516003704476137795166849228875"
  )

  def spec: Spec[Any, Any] = suite("Day00")(
    test("fib - base cases"):
      assertTrue(fib(0) == BigInt(0), fib(1) == BigInt(1), fib(2) == BigInt(1))
    ,
    test("fib - small"):
      assertTrue(fib(10) == BigInt(55), fib(20) == BigInt(6765))
    ,
    test("readFile - test"):
      readFile("inputs/Day00Test.txt").map(obtained => assertTrue(obtained == Seq(10, 1000)))
    ,
    test("readFile"):
      readFile("inputs/Day00.txt").map(obtained => assertTrue(obtained == Seq(10, 1000)))
    ,
    test("part1 - test"):
      readFile("inputs/Day00Test.txt").map(input => assertTrue(part1(input) == BigInt(55)))
    ,
    test("part1"):
      readFile("inputs/Day00.txt").map(input => assertTrue(part1(input) == BigInt(55)))
    ,
    test("part2 - test"):
      readFile("inputs/Day00Test.txt").map(input => assertTrue(part2(input) == part2Answer))
    ,
    test("part2"):
      readFile("inputs/Day00.txt").map(input => assertTrue(part2(input) == part2Answer)),
  )

end Day00Test

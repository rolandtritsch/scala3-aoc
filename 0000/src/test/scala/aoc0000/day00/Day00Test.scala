package aoc0000.day00

import aoc0000.util.AocError
import zio.test.*

object Day00Test extends ZIOSpecDefault:

  private val part2Answer = BigInt(
    "43466557686937456435688527675040625802564660517371780402481729089536555417949051890403879840079255169295922593080322634775209689623239873322471161642996440906533187938298969649928516003704476137795166849228875"
  )

  def spec: Spec[Any, Any] = suite("Day00")(
    test("parse - rejects empty, negative, malformed and overflowing indices"):
      assertTrue(
        Seq("", "-1", "10\n-1", "abc", "2147483648", "10\n\n1000")
          .forall(input => parse("sample", input).isLeft)
      )
    ,
    test("parse - single index and surrounding whitespace"):
      assertTrue(
        parse("sample", " 10 \n").map(input => (part1(input), part2(input))) ==
          Right((BigInt(55), BigInt(55)))
      )
    ,
    test("readFile - invalid index includes its location"):
      readFile("inputs/Day00Invalid.txt").either.map(result =>
        assertTrue(result.left.exists {
          case AocError.ParseError("inputs/Day00Invalid.txt", message) => message.contains("line 2")
          case _                                                       => false
        })
      )
    ,
    test("fib - base cases"):
      assertTrue(
        parseIndex("sample", "0").map(fib) == Right(BigInt(0)),
        parseIndex("sample", "1").map(fib) == Right(BigInt(1)),
        parseIndex("sample", "2").map(fib) == Right(BigInt(1)),
      )
    ,
    test("fib - small"):
      assertTrue(
        parseIndex("sample", "10").map(fib) == Right(BigInt(55)),
        parseIndex("sample", "20").map(fib) == Right(BigInt(6765)),
      )
    ,
    test("readFile - test"):
      readFile("inputs/Day00Test.txt")
        .map(obtained => assertTrue(parse("sample", "10\n1000") == Right(obtained)))
    ,
    test("readFile"):
      readFile("inputs/Day00.txt")
        .map(obtained => assertTrue(parse("sample", "10\n1000") == Right(obtained)))
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
  end spec

end Day00Test

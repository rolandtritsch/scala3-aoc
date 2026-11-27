package aoc2016.day12

import aoc2016.util.AocError
import zio.test.*

object Day12Test extends ZIOSpecDefault:

  import Instruction.*
  import Register.*
  import Value.*

  private val officialExample = """cpy 41 a
                                  |inc a
                                  |inc a
                                  |dec a
                                  |jnz a 2
                                  |dec a""".stripMargin

  def spec: Spec[Any, Any] = suite("Day12")(
    test("parse - maps the official example to typed instructions"):
      assertTrue(
        parse("sample", officialExample) == Right(Seq(
          Copy(Literal(41), A),
          Increment(A),
          Increment(A),
          Decrement(A),
          JumpNotZero(RegisterValue(A), 2),
          Decrement(A),
        ))
      )
    ,
    test("parse - permits whitespace between tokens"):
      assertTrue(
        parse("sample", "  cpy   -7 b \n\tjnz\tb\t2  ") ==
          Right(Seq(Copy(Literal(-7), B), JumpNotZero(RegisterValue(B), 2)))
      )
    ,
    test("parse - rejects malformed instructions and operands"):
      assertTrue(
        Seq(
          "nop a",
          "cpy 1 2",
          "inc 1",
          "dec a b",
          "jnz a nope",
          "jnz nope 1",
          "cpy 999999999999999999 a",
        ).forall(input => parse("sample", input).isLeft)
      )
    ,
    test("parse - reports the malformed instruction line"):
      assertTrue(parse("sample", "inc a\nnop a").left.exists {
        case AocError.ParseError("sample", message) => message.contains("line 2")
        case _                                      => false
      })
    ,
    test("part1 - official example"):
      assertTrue(parse("sample", officialExample).map(part1) == Right(42))
    ,
    test("part1 - a zero jump condition continues with the next instruction"):
      val instructions = Seq(JumpNotZero(Literal(0), 2), Increment(A), Copy(Literal(9), B))
      assertTrue(execute(instructions) == Registers(a = 1, b = 9, c = 0, d = 0))
    ,
    test("readFile and part1 - personal input"):
      readFile("inputs/Day12.txt")
        .map(input => assertTrue(input.size == 23 && part1(input) == 317993)),
  )
  end spec

end Day12Test

package aoc2015.day23

import aoc2015.util.AocError
import zio.test.*

object Day23Test extends ZIOSpecDefault:

  def spec: Spec[Any, Any] = suite("Day23")(
    test("parse - official example"):
      val input = """inc a
jio a, +2
tpl a
inc a"""
      assertTrue(
        parse("sample", input) == Right(Seq(
          Instruction.Inc(Register.A),
          Instruction.Jio(Register.A, 2),
          Instruction.Tpl(Register.A),
          Instruction.Inc(Register.A),
        ))
      )
    ,
    test("parse - puzzle input first few lines"):
      assertTrue(
        parse("sample", "jio a, +18\ninc a\ntpl a").map(_.take(3)) == Right(Seq(
          Instruction.Jio(Register.A, 18),
          Instruction.Inc(Register.A),
          Instruction.Tpl(Register.A),
        ))
      )
    ,
    test("readFile - typed instructions"):
      readFile("inputs/Day23.txt").map(instructions =>
        assertTrue(instructions.size == 47, instructions.head == Instruction.Jio(Register.A, 18))
      )
    ,
    test("readFile - missing resource"):
      readFile("inputs/Day23-missing.txt").either.map(result =>
        assertTrue(result.left.exists {
          case AocError.FileReadError("inputs/Day23-missing.txt", _) => true
          case _                                                     => false
        })
      )
    ,
    test("part1 - official example sets a to 2"):
      val input = """inc a
jio a, +2
tpl a
inc a"""
      assertTrue(parse("sample", input).map(part1) == Right(0))
    ,
    test("part1 - example with b register"):
      val input = """inc b
jio b, +2
tpl b
inc b"""
      assertTrue(parse("sample", input).map(part1) == Right(2))
    ,
    test("part1 - real input"):
      readFile("inputs/Day23.txt").map(input => assertTrue(part1(input) == 307))
    ,
    test("part2 - real input"):
      readFile("inputs/Day23.txt").map(input => assertTrue(part2(input) == 160)),
  )
  end spec

end Day23Test

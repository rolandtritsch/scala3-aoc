package aoc2015.day07

import aoc2015.util.AocError
import zio.test.*

object Day07Test extends ZIOSpecDefault:

  private def parseAll(lines: Seq[String]): Either[AocError, Seq[Instruction]] = lines.zipWithIndex
    .foldLeft[Either[AocError, Seq[Instruction]]](Right(Seq.empty)) {
      case (result, (line, index)) =>
        for
          instructions <- result
          instruction <- Instruction.parse("sample", line, index + 1)
        yield instructions :+ instruction
    }
  end parseAll

  private val sampleCircuit: Seq[String] = Seq(
    "123 -> x",
    "456 -> y",
    "x AND y -> d",
    "x OR y -> e",
    "x LSHIFT 2 -> f",
    "y RSHIFT 2 -> g",
    "NOT x -> h",
    "NOT y -> i",
  )

  def spec: Spec[Any, Any] = suite("Day07")(
    test("parse - literal, wire and gates"):
      assertTrue(
        Instruction.parse("sample", "123 -> x").map(i => (i.gate, i.output)) ==
          Right((Gate.Literal(123), "x")),
        Instruction.parse("sample", "lx -> a").map(i => (i.gate, i.output)) ==
          Right((Gate.Wire("lx"), "a")),
        Instruction.parse("sample", "x AND y -> z").map(i => (i.gate, i.output)) ==
          Right((Gate.And(Operand.Ref("x"), Operand.Ref("y")), "z")),
        Instruction.parse("sample", "x OR y -> z").map(i => (i.gate, i.output)) ==
          Right((Gate.Or(Operand.Ref("x"), Operand.Ref("y")), "z")),
        Instruction.parse("sample", "1 AND io -> ip").map(i => (i.gate, i.output)) ==
          Right((Gate.And(Operand.Lit(1), Operand.Ref("io")), "ip")),
        Instruction.parse("sample", "x LSHIFT 2 -> z").map(i => (i.gate, i.output)) ==
          Right((Gate.LShift(Operand.Ref("x"), 2), "z")),
        Instruction.parse("sample", "1 LSHIFT 15 -> x").map(i => (i.gate, i.output)) ==
          Right((Gate.LShift(Operand.Lit(1), 15), "x")),
        Instruction.parse("sample", "x RSHIFT 3 -> z").map(i => (i.gate, i.output)) ==
          Right((Gate.RShift(Operand.Ref("x"), 3), "z")),
        Instruction.parse("sample", "NOT x -> z").map(i => (i.gate, i.output)) ==
          Right((Gate.Not("x"), "z")),
      )
    ,
    test("parse - rejects malformed lines"):
      assertTrue(
        Seq("", "x AND -> z", "NOT -> z", "x XOR y -> z", "x ->> z", "-> z")
          .forall(line => Instruction.parse("sample", line).isLeft)
      )
    ,
    test("parse - output wire must be a lowercase wire"):
      assertTrue(
        Instruction.parse("sample", "123 -> 1").isLeft,
        Instruction.parse("sample", "x -> A").isLeft,
      )
    ,
    test("part1 - official sample every wire"):
      parseAll(sampleCircuit).map { instructions =>
        assertTrue(
          Seq("d", "e", "f", "g", "h", "i", "x", "y").map(signalOf(_, instructions)) ==
            Seq(72, 507, 492, 114, 65412, 65079, 123, 456)
        )
      }
    ,
    test("part1 - passthrough and literal chains"):
      parseAll(Seq("123 -> x", "x -> y", "y -> a")).map(instr => assertTrue(part1(instr) == 123))
    ,
    test("part1 - 16-bit masking on NOT zero"):
      parseAll(Seq("0 -> x", "NOT x -> y", "y -> a"))
        .map(instr => assertTrue(part1(instr) == 65535))
    ,
    test("part1 - left shift takes a literal operand"):
      parseAll(Seq("1 LSHIFT 15 -> x", "x -> a")).map(instr => assertTrue(part1(instr) == 32768))
    ,
    test("readFile - invalid gate reports line"):
      readFile("inputs/Day07Invalid.txt").either.map(result =>
        assertTrue(result.left.exists {
          case AocError.ParseError("inputs/Day07Invalid.txt", message) => message.contains("line 2")
          case _                                                       => false
        })
      )
    ,
    test("readFile - duplicate output wire rejected"):
      readFile("inputs/Day07Duplicate.txt").either.map(result =>
        assertTrue(result.left.exists {
          case AocError.ValidationError(message) => message.contains("assigned more than once")
          case _                                 => false
        })
      )
    ,
    test("readFile - real input loads and provides wire a"):
      readFile("inputs/Day07.txt")
        .map(instructions => assertTrue(instructions.map(_.output).contains("a"))),
  )
  end spec

end Day07Test

package aoc2015.day07

import aoc2015.util.AocError

/** One instruction: a gate bound to an output wire.
  *
  * Each wire receives a signal from exactly one source, though it may feed many others.
  */
final case class Instruction private (gate: Gate, output: String)

object Instruction:

  private def isWire(token: String): Boolean = token.nonEmpty &&
    token.forall(c => c >= 'a' && c <= 'z')

  private def parseOperand(
      file: String,
      token: String,
      lineNumber: Int,
      line: String,
  ): Either[AocError, Operand] =
    if isWire(token) then Right(Operand.Ref(token))
    else
      token.toIntOption.toRight(
        AocError
          .ParseError(file, s"line $lineNumber: expected a wire or literal, got \"$token\": $line")
      ).map(Operand.Lit.apply)
  end parseOperand

  private def parseWire(
      file: String,
      token: String,
      lineNumber: Int,
      line: String,
  ): Either[AocError, String] =
    if isWire(token) then Right(token)
    else
      Left(AocError.ParseError(file, s"line $lineNumber: expected a wire, got \"$token\": $line"))
  end parseWire

  private def parseShift(
      file: String,
      token: String,
      lineNumber: Int,
      line: String,
  ): Either[AocError, Int] = token.toIntOption
    .toRight(AocError.ParseError(file, s"line $lineNumber: shift amount must be a literal: $line"))
  end parseShift

  /** Parse one `rhs -> wire` instruction line.
    *
    * Supported forms, matching the puzzle's description:
    *   - `123 -> x` (literal)
    *   - `x -> y` (wire passthrough)
    *   - `x AND y -> z`, `x OR y -> z` (either operand may be a literal or a wire)
    *   - `x LSHIFT n -> z`, `x RSHIFT n -> z` (left operand literal or wire; amount a literal)
    *   - `NOT x -> z`
    */
  def parse(filename: String, line: String, lineNumber: Int = 1): Either[AocError, Instruction] =
    def invalid(reason: String): Left[AocError, Nothing] =
      Left(AocError.ParseError(filename, s"line $lineNumber: $reason: $line"))

    line.trim.split("->", 2).map(_.trim) match
      case Array(expr, output) =>
        if !isWire(output) then invalid("bad output wire")
        else parseGate(expr, filename, lineNumber, line).map(Instruction(_, output))
      case _ => invalid("expected \" -> \" separator")
    end match

  end parse

  private def parseGate(
      expr: String,
      filename: String,
      lineNumber: Int,
      line: String,
  ): Either[AocError, Gate] =
    def invalid(reason: String): Left[AocError, Nothing] =
      Left(AocError.ParseError(filename, s"line $lineNumber: $reason: $line"))

    expr.split("\\s+").toSeq match
      case Seq(single)    => parseSource(single, filename, lineNumber, line)
      case Seq("NOT", op) => parseWire(filename, op, lineNumber, line).map(Gate.Not.apply)
      case Seq(l, op, r) if op == "AND" =>
        for
          left <- parseOperand(filename, l, lineNumber, line)
          right <- parseOperand(filename, r, lineNumber, line)
        yield Gate.And(left, right)
      case Seq(l, op, r) if op == "OR" =>
        for
          left <- parseOperand(filename, l, lineNumber, line)
          right <- parseOperand(filename, r, lineNumber, line)
        yield Gate.Or(left, right)
      case Seq(l, op, amount) if op == "LSHIFT" || op == "RSHIFT" =>
        for
          left <- parseOperand(filename, l, lineNumber, line)
          shift <- parseShift(filename, amount, lineNumber, line)
        yield if op == "LSHIFT" then Gate.LShift(left, shift) else Gate.RShift(left, shift)
      case _ => invalid("unrecognized gate")
    end match

  end parseGate

  private def parseSource(
      token: String,
      filename: String,
      lineNumber: Int,
      line: String,
  ): Either[AocError, Gate] =
    if isWire(token) then Right(Gate.Wire(token))
    else
      token.toIntOption
        .toRight(AocError.ParseError(filename, s"line $lineNumber: literal out of range: $line"))
        .map(Gate.Literal.apply)
  end parseSource

end Instruction

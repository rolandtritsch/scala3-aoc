package aoc2016.day12

import aoc2016.util.AocError

private val IntegerPattern = """[+-]?\d+""".r

def parse(filename: String, input: String): Either[AocError, Seq[Instruction]] = input.linesIterator
  .toSeq.zipWithIndex.foldLeft[Either[AocError, Vector[Instruction]]](Right(Vector.empty)) {
    case (result, (line, index)) =>
      for
        instructions <- result
        instruction <- parseInstruction(filename, line, index + 1)
      yield instructions :+ instruction
  }
end parse

private def parseInstruction(
    filename: String,
    line: String,
    lineNumber: Int,
): Either[AocError, Instruction] = line.trim.split("\\s+").toSeq match
  case Seq("cpy", source, target) =>
    for
      value <- parseValue(filename, lineNumber, source)
      register <- parseRegister(filename, lineNumber, target)
    yield Instruction.Copy(value, register)
  case Seq("inc", register) => parseRegister(filename, lineNumber, register)
      .map(Instruction.Increment(_))
  case Seq("dec", register) => parseRegister(filename, lineNumber, register)
      .map(Instruction.Decrement(_))
  case Seq("jnz", source, offset) =>
    for
      value <- parseValue(filename, lineNumber, source)
      distance <- offset.toIntOption
        .toRight(parseError(filename, lineNumber, s"'$offset' is not a valid jump offset"))
    yield Instruction.JumpNotZero(value, distance)
  case _ => Left(parseError(filename, lineNumber, "unrecognized assembunny instruction"))
end parseInstruction

private def parseValue(filename: String, lineNumber: Int, text: String): Either[AocError, Value] =
  text.toIntOption match
    case Some(value) => Right(Value.Literal(value))
    case None        => text match
        case IntegerPattern() =>
          Left(parseError(filename, lineNumber, s"integer '$text' is out of range"))
        case _ => parseRegister(filename, lineNumber, text).map(Value.RegisterValue(_))
end parseValue

private def parseRegister(
    filename: String,
    lineNumber: Int,
    text: String,
): Either[AocError, Register] = text match
  case "a" => Right(Register.A)
  case "b" => Right(Register.B)
  case "c" => Right(Register.C)
  case "d" => Right(Register.D)
  case _   => Left(parseError(filename, lineNumber, s"'$text' is not a register"))
end parseRegister

private def parseError(filename: String, lineNumber: Int, message: String): AocError.ParseError =
  AocError.ParseError(filename, s"line $lineNumber: $message")

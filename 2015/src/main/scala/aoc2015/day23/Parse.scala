package aoc2015.day23

import aoc2015.util.AocError

def parse(filename: String, input: String): Either[AocError, Seq[Instruction]] =
  val lines = input.linesIterator.toSeq
  lines.zipWithIndex.foldLeft[Either[AocError, Seq[Instruction]]](Right(Seq.empty)) {
    case (result, (line, index)) => result
        .flatMap(instructions => parseLine(filename, line.trim, index + 1).map(instructions :+ _))
  }
end parse

private def parseLine(filename: String, line: String, lineNum: Int): Either[AocError, Instruction] =
  val parts = line.split("[ ,]+")
  if parts.isEmpty then Left(AocError.ParseError(filename, s"empty line at $lineNum"))
  else
    parts(0) match
      case "hlf" => parseRegister(parts.lift(1), filename, lineNum).map(Instruction.Hlf(_))
      case "tpl" => parseRegister(parts.lift(1), filename, lineNum).map(Instruction.Tpl(_))
      case "inc" => parseRegister(parts.lift(1), filename, lineNum).map(Instruction.Inc(_))
      case "jmp" => parseOffset(parts.lift(1), filename, lineNum).map(Instruction.Jmp(_))
      case "jie" =>
        for
          reg <- parseRegister(parts.lift(1), filename, lineNum)
          offset <- parseOffset(parts.lift(2), filename, lineNum)
        yield Instruction.Jie(reg, offset)
      case "jio" =>
        for
          reg <- parseRegister(parts.lift(1), filename, lineNum)
          offset <- parseOffset(parts.lift(2), filename, lineNum)
        yield Instruction.Jio(reg, offset)
      case other =>
        Left(AocError.ParseError(filename, s"unknown instruction '$other' at line $lineNum"))
end parseLine

private def parseRegister(
    opt: Option[String],
    filename: String,
    lineNum: Int,
): Either[AocError, Register] = opt match
  case Some("a")   => Right(Register.A)
  case Some("b")   => Right(Register.B)
  case Some(other) =>
    Left(AocError.ParseError(filename, s"invalid register '$other' at line $lineNum"))
  case None => Left(AocError.ParseError(filename, s"missing register at line $lineNum"))
end parseRegister

private def parseOffset(
    opt: Option[String],
    filename: String,
    lineNum: Int,
): Either[AocError, Int] = opt match
  case Some(str) =>
    try Right(str.toInt)
    catch
      case _: NumberFormatException =>
        Left(AocError.ParseError(filename, s"invalid offset '$str' at line $lineNum"))
  case None => Left(AocError.ParseError(filename, s"missing offset at line $lineNum"))
end parseOffset

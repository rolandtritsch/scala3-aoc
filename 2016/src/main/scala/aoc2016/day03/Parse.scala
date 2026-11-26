package aoc2016.day03

import aoc2016.util.AocError

def parse(filename: String, input: String): Either[AocError, Seq[Triangle]] =
  val lines = input.split("\\R", -1).toSeq
  val triangleLines = if lines.lastOption.contains("") then lines.dropRight(1) else lines
  if triangleLines.isEmpty then
    Left(AocError.ParseError(filename, "expected at least one triangle"))
  else
    triangleLines.zipWithIndex.foldLeft[Either[AocError, Seq[Triangle]]](Right(Seq.empty)) {
      case (result, (line, index)) =>
        for
          triangles <- result
          triangle <- parseLine(filename, line, index + 1)
        yield triangle +: triangles
    }.map(_.reverse)
end parse

private def parseLine(filename: String, line: String, lineNumber: Int): Either[AocError, Triangle] =
  line.trim.split("\\s+").toSeq match
    case Seq(first, second, third) =>
      for
        a <- parseSide(filename, first, lineNumber, 1)
        b <- parseSide(filename, second, lineNumber, 2)
        c <- parseSide(filename, third, lineNumber, 3)
      yield Triangle(a, b, c)
    case _ => Left(AocError.ParseError(
        filename,
        s"line $lineNumber: expected exactly three whitespace-separated side lengths",
      ))
end parseLine

private def parseSide(
    filename: String,
    input: String,
    lineNumber: Int,
    position: Int,
): Either[AocError, Int] = input.toIntOption.filter(_ > 0).toRight(AocError.ParseError(
  filename,
  s"line $lineNumber, side $position: expected a positive 32-bit integer; found '$input'",
))
end parseSide

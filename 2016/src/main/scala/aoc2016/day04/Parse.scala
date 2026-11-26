package aoc2016.day04

import aoc2016.util.AocError

private val RoomLine = """([a-z]+(?:-[a-z]+)*)-([0-9]+)\[([a-z]{5})]""".r

def parse(filename: String, input: String): Either[AocError, Seq[Room]] =
  val lines = input.split("\\R", -1).toSeq
  val roomLines = if lines.lastOption.contains("") then lines.dropRight(1) else lines
  if roomLines.isEmpty then Left(AocError.ParseError(filename, "expected at least one room"))
  else
    roomLines.zipWithIndex.foldLeft[Either[AocError, Seq[Room]]](Right(Seq.empty)) {
      case (result, (line, index)) =>
        for
          rooms <- result
          room <- parseLine(filename, line, index + 1)
        yield room +: rooms
    }.map(_.reverse)
end parse

private def parseLine(filename: String, line: String, lineNumber: Int): Either[AocError, Room] =
  line match
    case RoomLine(name, sectorId, checksum) => parseSectorId(filename, sectorId, lineNumber)
        .map(id => Room(name.split("-").toSeq, id, checksum))
    case _ => Left(AocError.ParseError(
        filename,
        s"line $lineNumber: expected lowercase dash-separated names, a sector ID, and a five-letter checksum",
      ))
end parseLine

private def parseSectorId(filename: String, input: String, lineNumber: Int): Either[AocError, Int] =
  input.toIntOption.toRight(AocError.ParseError(
    filename,
    s"line $lineNumber: expected a non-negative 32-bit sector ID; found '$input'",
  ))

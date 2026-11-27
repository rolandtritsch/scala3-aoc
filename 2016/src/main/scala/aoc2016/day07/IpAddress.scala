package aoc2016.day07

import aoc2016.util.AocError

/** An IPv7 address separated into text outside and inside square brackets. */
final case class IpAddress(supernets: Seq[String], hypernets: Seq[String])

private val AddressLine = """(?:[a-z]+|\[[a-z]+])+""".r
private val Segment = """[a-z]+|\[[a-z]+]""".r

def parse(filename: String, input: String): Either[AocError, Seq[IpAddress]] =
  val lines = input.linesIterator.toSeq
  if lines.isEmpty then Left(AocError.ParseError(filename, "expected at least one IP address"))
  else
    lines.zipWithIndex.foldLeft[Either[AocError, Seq[IpAddress]]](Right(Seq.empty)) {
      case (result, (line, index)) =>
        for
          addresses <- result
          address <- parseLine(filename, line, index + 1)
        yield address +: addresses
    }.map(_.reverse)
end parse

private def parseLine(
    filename: String,
    line: String,
    lineNumber: Int,
): Either[AocError, IpAddress] = line match
  case AddressLine() =>
    val (bracketed, plain) = Segment.findAllIn(line).toSeq.partition(_.startsWith("["))
    Right(IpAddress(
      supernets = plain,
      hypernets = bracketed.map(segment => segment.substring(1, segment.length - 1)),
    ))
  case _ => Left(AocError.ParseError(
      filename,
      s"line $lineNumber: expected lowercase letters and nonempty square-bracketed hypernet sequences",
    ))
end parseLine

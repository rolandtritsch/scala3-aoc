package aoc2016.day20

import aoc2016.util.AocError

/** Parse the firewall blacklist: one `start-end` inclusive range per non-empty line.
  *
  * Both bounds are 32-bit unsigned integers, so they are parsed as `Long` and validated to fit in
  * `0 .. 4294967295`. A reversed or out-of-range bound is a typed parse error, never a thrown
  * exception.
  */
def parse(filename: String, input: String): Either[AocError, Input] =
  val lines = input.linesIterator.toSeq.filterNot(_.trim.isEmpty).zipWithIndex
  if lines.isEmpty then Left(AocError.ParseError(filename, "expected at least one blocked range"))
  else
    val parsed = lines.map((line, index) => parseRange(filename, line.trim, index + 1))
    val firstError = parsed.collectFirst { case Left(error) => error }
    firstError match
      case Some(error) => Left(error)
      case None        => Right(Input(parsed.collect { case Right(range) => range }))
  end if
end parse

/** Parse a single `start-end` line, reporting the 1-based line number on failure. */
private def parseRange(
    filename: String,
    line: String,
    lineNumber: Int,
): Either[AocError, BlockedRange] = line.split('-') match
  case Array(startText, endText) =>
    for
      start <- parseBound(filename, startText, lineNumber)
      end <- parseBound(filename, endText, lineNumber)
      range <-
        if start <= end then Right(BlockedRange(start, end))
        else Left(AocError.ParseError(filename, s"line $lineNumber: start $start exceeds end $end"))
    yield range
  case _ => Left(AocError.ParseError(filename, s"line $lineNumber: expected 'start-end': $line"))
end parseRange

/** Parse one 32-bit unsigned bound, rejecting negatives and values above `4294967295`. */
private def parseBound(filename: String, text: String, lineNumber: Int): Either[AocError, Long] =
  text.toLongOption.filter(value => value >= 0L && value <= 0xffffffffL)
    .toRight(AocError.ParseError(filename, s"line $lineNumber: invalid 32-bit bound: $text"))

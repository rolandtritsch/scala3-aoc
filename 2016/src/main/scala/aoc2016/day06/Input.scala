package aoc2016.day06

import aoc2016.util.AocError

/** A non-empty sequence of equal-length, lowercase signals; construct through [[parse]]. */
opaque type Recording = Seq[String]

extension (recording: Recording) def signals: Seq[String] = recording

def parse(filename: String, input: String): Either[AocError, Recording] =
  val signals = input.linesIterator.toSeq
  signals.headOption match
    case None        => Left(AocError.ParseError(filename, "expected at least one signal"))
    case Some(first) => signals.zipWithIndex
        .foldLeft[Either[AocError, Recording]](Right(Seq.empty)) { case (result, (signal, index)) =>
          for
            recording <- result
            valid <- parseSignal(filename, signal, first.length, index + 1)
          yield recording :+ valid
        }
end parse

private def parseSignal(
    filename: String,
    signal: String,
    width: Int,
    lineNumber: Int,
): Either[AocError, String] =
  if signal.isEmpty || !signal.forall(character => character >= 'a' && character <= 'z') then
    Left(AocError.ParseError(filename, s"line $lineNumber: expected only lowercase letters"))
  else if signal.length != width then
    Left(AocError.ParseError(
      filename,
      s"line $lineNumber: expected $width characters, found ${signal.length}",
    ))
  else Right(signal)
end parseSignal

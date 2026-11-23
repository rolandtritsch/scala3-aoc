package aoc2015.util

/** Parse non-empty lines in order, retaining original line numbers for errors. */
def parseLines[A](filename: String, input: String)(
    parseLine: (String, String, Int) => Either[AocError, A]
): Either[AocError, Seq[A]] = input.linesIterator.zipWithIndex.filter(_._1.trim.nonEmpty)
  .foldLeft[Either[AocError, Seq[A]]](Right(Seq.empty)) { case (result, (line, index)) =>
    for
      values <- result
      value <- parseLine(filename, line, index + 1)
    yield value +: values
  }.map(_.reverse)

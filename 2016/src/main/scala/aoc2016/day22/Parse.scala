package aoc2016.day22

import aoc2016.util.AocError

/** Parse the `df` report: a header, then one `/dev/grid/node-xX-yY` line per node.
  *
  * The two header lines (`root@...` and `Filesystem ...`) are recognised and skipped; every other
  * non-empty line must name a node and carry `Size Used Avail Use%` columns. Sizes are reported in
  * terabytes with a trailing `T`. Any unrecognised line is a typed parse error naming its 1-based
  * line number, never a silently discarded instruction.
  */
def parse(filename: String, input: String): Either[AocError, Input] =
  val lines = input.linesIterator.toSeq.zipWithIndex.filterNot((line, _) => line.trim.isEmpty)
  val parsed = lines.map((line, index) => parseLine(filename, line.trim, index + 1))
  parsed.collectFirst { case Left(error) => error } match
    case Some(error) => Left(error)
    case None        =>
      val nodes = parsed.collect { case Right(Some(node)) => node }
      if nodes.isEmpty then Left(AocError.ParseError(filename, "expected at least one node"))
      else Right(Input(nodes))
  end match
end parse

/** Parse one report line, returning `None` for the two header lines. */
private def parseLine(
    filename: String,
    line: String,
    lineNumber: Int,
): Either[AocError, Option[Node]] =
  if line.startsWith("root@") || line.startsWith("Filesystem") then Right(None)
  else parseNode(filename, line, lineNumber).map(Some.apply)
end parseLine

/** Parse a `/dev/grid/node-xX-yY Size Used Avail Use%` line into a `Node`. */
private def parseNode(filename: String, line: String, lineNumber: Int): Either[AocError, Node] =
  val fields = line.split("\\s+")
  fields match
    case Array(path, sizeText, usedText, availText, _) =>
      for
        (x, y) <- parsePosition(filename, path, lineNumber)
        size <- parseSize(filename, sizeText, lineNumber)
        used <- parseSize(filename, usedText, lineNumber)
        avail <- parseSize(filename, availText, lineNumber)
      yield Node(x, y, size, used, avail)
    case _ =>
      Left(AocError.ParseError(filename, s"line $lineNumber: unrecognized node line: $line"))
end parseNode

/** Extract the `(x, y)` coordinates from a `/dev/grid/node-xX-yY` path. */
private def parsePosition(
    filename: String,
    path: String,
    lineNumber: Int,
): Either[AocError, (Int, Int)] =
  val pattern = "/dev/grid/node-x([0-9]+)-y([0-9]+)".r
  path match
    case pattern(xText, yText) =>
      for
        x <- xText.toIntOption
          .toRight(AocError.ParseError(filename, s"line $lineNumber: invalid x coordinate: $xText"))
        y <- yText.toIntOption
          .toRight(AocError.ParseError(filename, s"line $lineNumber: invalid y coordinate: $yText"))
      yield (x, y)
    case _ =>
      Left(AocError.ParseError(filename, s"line $lineNumber: unrecognized node path: $path"))
end parsePosition

/** Parse a terabyte quantity such as `85T` into a `Long`. */
private def parseSize(filename: String, text: String, lineNumber: Int): Either[AocError, Long] =
  val digits = text.stripSuffix("T")
  digits.toLongOption.filter(_ >= 0L)
    .toRight(AocError.ParseError(filename, s"line $lineNumber: invalid size: $text"))

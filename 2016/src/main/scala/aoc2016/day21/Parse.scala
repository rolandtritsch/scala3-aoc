package aoc2016.day21

import aoc2016.util.AocError

/** Parse the scrambling operations: one instruction per non-empty line.
  *
  * Each recognised instruction becomes a typed `Operation`; anything else is a typed parse error
  * naming the 1-based line number. Parsing stays pure and separately testable.
  */
def parse(filename: String, input: String): Either[AocError, Input] =
  val lines = input.linesIterator.toSeq.zipWithIndex.filterNot((line, _) => line.trim.isEmpty)
  if lines.isEmpty then Left(AocError.ParseError(filename, "expected at least one operation"))
  else
    val parsed = lines.map((line, index) => parseLine(filename, line.trim, index + 1))
    parsed.collectFirst { case Left(error) => error } match
      case Some(error) => Left(error)
      case None        => Right(Input(parsed.collect { case Right(operation) => operation }))
  end if
end parse

/** Parse one instruction line, reporting the 1-based line number on failure. */
private def parseLine(
    filename: String,
    line: String,
    lineNumber: Int,
): Either[AocError, Operation] =
  val tokens = line.split(' ')
  tokens match
    case Array("swap", "position", x, "with", "position", y) => parseIndex(filename, x, lineNumber)
        .flatMap(a => parseIndex(filename, y, lineNumber).map(b => Operation.SwapPosition(a, b)))
    case Array("swap", "letter", x, "with", "letter", y) => parseLetter(filename, x, y, lineNumber)
        .map(Operation.SwapLetter.apply)
    case Array("rotate", direction, steps, "step" | "steps") =>
      parseRotation(filename, direction, lineNumber)
        .flatMap(dir => parseCount(filename, steps, lineNumber).map(n => Operation.Rotate(n, dir)))
    case Array("rotate", "based", "on", "position", "of", "letter", letter) =>
      parseLetterChar(filename, letter, lineNumber).map(Operation.RotateLetter(_))
    case Array("reverse", "positions", x, "through", y) => parseIndex(filename, x, lineNumber)
        .flatMap(a => parseIndex(filename, y, lineNumber).map(b => Operation.Reverse(a, b)))
    case Array("move", "position", x, "to", "position", y) => parseIndex(filename, x, lineNumber)
        .flatMap(a => parseIndex(filename, y, lineNumber).map(b => Operation.Move(a, b)))
    case _ =>
      Left(AocError.ParseError(filename, s"line $lineNumber: unrecognized operation: $line"))
end parseLine

/** Parse a non-negative position index. */
private def parseIndex(filename: String, text: String, lineNumber: Int): Either[AocError, Int] =
  text.toIntOption.filter(_ >= 0)
    .toRight(AocError.ParseError(filename, s"line $lineNumber: invalid position: $text"))

/** Parse a non-negative step count. */
private def parseCount(filename: String, text: String, lineNumber: Int): Either[AocError, Int] =
  text.toIntOption.filter(_ >= 0)
    .toRight(AocError.ParseError(filename, s"line $lineNumber: invalid step count: $text"))

/** Parse a single-letter operand, requiring exactly one character. */
private def parseLetterChar(
    filename: String,
    text: String,
    lineNumber: Int,
): Either[AocError, Char] =
  if text.length == 1 then Right(text.charAt(0))
  else Left(AocError.ParseError(filename, s"line $lineNumber: expected a single letter: $text"))
end parseLetterChar

/** Parse the two letters of a `swap letter X with letter Y` instruction. */
private def parseLetter(
    filename: String,
    x: String,
    y: String,
    lineNumber: Int,
): Either[AocError, (Char, Char)] =
  for
    a <- parseLetterChar(filename, x, lineNumber)
    b <- parseLetterChar(filename, y, lineNumber)
  yield (a, b)
end parseLetter

/** Parse a rotation direction word. */
private def parseRotation(
    filename: String,
    direction: String,
    lineNumber: Int,
): Either[AocError, Rotation] = direction match
  case "left"  => Right(Rotation.Left)
  case "right" => Right(Rotation.Right)
  case _ => Left(AocError.ParseError(filename, s"line $lineNumber: invalid rotation: $direction"))
end parseRotation

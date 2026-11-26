package aoc2015.day19

import aoc2015.util.AocError
import aoc2015.util.readResource
import zio.*

/** Load and validate Day19 input for the solution. */
def readFile(filename: String): ZIO[Any, AocError, Input] =
  readResource(filename) { (name, input) =>
    val lines = input.linesIterator.filter(_.trim.nonEmpty).toSeq
    if lines.isEmpty then Left(AocError.ParseError(name, "empty input"))
    else
      val (ruleLines, moleculeLines) = lines.splitAt(lines.length - 1)
      if moleculeLines.length != 1 then
        Left(AocError.ParseError(
          name,
          s"expected exactly one medicine molecule line, got ${moleculeLines.length}",
        ))
      else parseRules(name, ruleLines).map(rules => Input(rules, moleculeLines.head.trim))
  }
end readFile

/** Parse replacement rules from lines like "Al => ThF". */
private def parseRules(filename: String, lines: Seq[String]): Either[AocError, Seq[Replacement]] =
  lines.foldLeft[Either[AocError, Seq[Replacement]]](Right(Seq.empty)) { (acc, line) =>
    for
      rules <- acc
      rule <- parseRule(filename, line)
    yield rule +: rules
  }.map(_.reverse)
end parseRules

/** Parse a single replacement rule line. */
private def parseRule(filename: String, line: String): Either[AocError, Replacement] = line
  .split(" => ").toSeq match
  case Seq(from, to) if from.nonEmpty && to.nonEmpty => Right(Replacement(from, to))
  case _ => Left(AocError.ParseError(filename, s"invalid rule format: $line"))

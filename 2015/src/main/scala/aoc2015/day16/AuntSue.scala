package aoc2015.day16

import aoc2015.util.AocError

/** One Aunt Sue and the facts remembered about her.
  *
  * Each input line names a Sue by number and lists the counts of the attributes remembered about
  * her. An attribute absent from the list is simply unknown, not zero.
  *
  * @param number
  *   the one-based index that identifies this Sue.
  * @param remembered
  *   the remembered attribute counts, keyed by attribute; each attribute appears at most once.
  */
final case class AuntSue(number: Int, remembered: Map[Attribute, Int])

object AuntSue:

  private val Header = """Sue (\d+): (.+)""".r
  private val Fact = """([a-z]+): (\d+)""".r

  /** Parse one `Sue N: label: count, ...` line.
    *
    * The header names the Sue's number and the remainder lists remembered facts as comma-separated
    * `label: count` pairs. Every label must be a known attribute, every count a non-negative
    * integer, and no attribute may be repeated.
    *
    * @param filename
    *   the resource name, used for error messages.
    * @param line
    *   the raw input line.
    * @param lineNumber
    *   the one-based line number, used for error messages.
    * @return
    *   the parsed Sue, or a parse error.
    */
  def parse(filename: String, line: String, lineNumber: Int = 1): Either[AocError, AuntSue] =
    def invalid(reason: String): Left[AocError, Nothing] =
      Left(AocError.ParseError(filename, s"line $lineNumber: $reason: $line"))

    line.trim match
      case Header(numberText, factsText) => numberText.toIntOption match
          case Some(number) if number >= 1 =>
            parseFacts(filename, line, lineNumber, factsText).map(facts => AuntSue(number, facts))
          case _ => invalid("Sue number must be a positive integer")
      case _ => invalid("expected \"Sue N: label: count, ...\"")
    end match
  end parse

  private def parseFacts(
      filename: String,
      line: String,
      lineNumber: Int,
      factsText: String,
  ): Either[AocError, Map[Attribute, Int]] = factsText.split(", ").toSeq
    .foldLeft[Either[AocError, Map[Attribute, Int]]](Right(Map.empty)) { (result, item) =>
      result.flatMap { facts =>
        parseFact(filename, line, lineNumber, item).flatMap { (attribute, value) =>
          if facts.contains(attribute) then
            Left(AocError.ParseError(
              filename,
              s"line $lineNumber: duplicate attribute ${attribute.label}: $line",
            ))
          else Right(facts.updated(attribute, value))
        }
      }
    }
  end parseFacts

  private def parseFact(
      filename: String,
      line: String,
      lineNumber: Int,
      item: String,
  ): Either[AocError, (Attribute, Int)] =
    item match
      case Fact(label, valueText) =>
        for
          attribute <- Attribute.fromLabel(label).toRight(
            AocError.ParseError(filename, s"line $lineNumber: unknown attribute $label: $line")
          )
          value <- valueText.toIntOption.toRight(AocError.ParseError(
            filename,
            s"line $lineNumber: count for $label must be a non-negative integer: $line",
          ))
        yield (attribute, value)
      case _ => Left(
          AocError
            .ParseError(filename, s"line $lineNumber: expected \"label: count\", got: $item: $line")
        )
    end match
  end parseFact

end AuntSue

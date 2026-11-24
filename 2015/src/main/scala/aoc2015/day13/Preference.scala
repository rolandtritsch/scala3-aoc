package aoc2015.day13

import aoc2015.util.AocError

/** A directed happiness preference: how much one person likes sitting next to another.
  *
  * Each input line names the person affected and the person they sit next to, together with a
  * signed happiness change. The value is positive for "gain" and negative for "lose". Unlike the
  * distances in Day09, these preferences are directed: Alice sitting next to Bob is a different
  * amount than Bob sitting next to Alice.
  *
  * @param person
  *   the person whose happiness changes.
  * @param neighbor
  *   the person sitting adjacent to `person`.
  * @param change
  *   the signed happiness change `person` experiences next to `neighbor`.
  */
final case class Preference(person: String, neighbor: String, change: Int)

object Preference:

  private val Word = """(.+) would (gain|lose) (\d+) happiness units by sitting next to (.+)\.""".r

  /** Parse one `X would gain N happiness units by sitting next to Y.` line.
    *
    * The amount is a non-negative integer; `gain` keeps it positive, `lose` negates it. The person
    * and neighbor must be distinct.
    *
    * @param filename
    *   the resource name, used for error messages.
    * @param line
    *   the raw input line.
    * @param lineNumber
    *   the one-based line number, used for error messages.
    * @return
    *   the parsed preference, or a parse error.
    */
  def parse(filename: String, line: String, lineNumber: Int = 1): Either[AocError, Preference] =
    def invalid(reason: String): Left[AocError, Nothing] =
      Left(AocError.ParseError(filename, s"line $lineNumber: $reason: $line"))

    line.trim match
      case Word(person, kind, amount, neighbor) =>
        val change = if kind == "gain" then amount.toInt else -amount.toInt
        if person == neighbor then invalid("person and neighbor must differ")
        else Right(Preference(person, neighbor, change))
      case _ => invalid("expected \"A would gain/lose N happiness units by sitting next to B.\"")
    end match
  end parse

end Preference

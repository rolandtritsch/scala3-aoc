package aoc2015.day10

import aoc2015.util.AocError

/** A non-empty sequence of decimal digits, the domain value for the look-and-say game. */
opaque type Digits = Vector[Int]

object Digits:

  /** Parse `input` as one non-empty sequence of decimal digits, ignoring surrounding whitespace.
    *
    * @param filename
    *   the resource name, used for error messages.
    * @param input
    *   the raw input.
    * @return
    *   the parsed sequence of digits, or a parse error describing the first non-digit character.
    */
  def parse(filename: String, input: String): Either[AocError, Digits] =
    val trimmed = input.trim
    if trimmed.isEmpty then
      Left(AocError.ParseError(filename, "expected a non-empty sequence of decimal digits"))
    else
      trimmed.toVector.zipWithIndex.foldLeft[Either[AocError, Vector[Int]]](Right(Vector.empty)) {
        case (result, (character, index)) => result.flatMap { digits =>
            if character >= '0' && character <= '9' then Right(digits :+ (character - '0'))
            else
              Left(
                AocError
                  .ParseError(filename, s"position ${index + 1}: unexpected character '$character'")
              )
          }
      }

  end parse

  /** The number of digits in the sequence. */
  def length(digits: Digits): Int = digits.length

end Digits

/** Apply one look-and-say step to a sequence of digits.
  *
  * Each maximal run of identical digits is replaced by the decimal representation of the run's
  * length followed by the digit itself. For example, `1221` becomes `112211` (one 1, two 2s, one
  * 1).
  */
def lookAndSay(digits: Digits): Digits =
  val runs = digits.foldLeft(Vector.empty[(Int, Int)]) { (acc, digit) =>
    acc match
      case init :+ (count, previous) if previous == digit => init :+ (count + 1, previous)
      case _                                              => acc :+ (1, digit)
  }
  runs.flatMap((count, digit) => decimalDigits(count) :+ digit)

end lookAndSay

/** The decimal digits of a positive integer, most significant first. */
private def decimalDigits(n: Int): Vector[Int] =
  if n < 10 then Vector(n) else decimalDigits(n / 10) :+ (n % 10)

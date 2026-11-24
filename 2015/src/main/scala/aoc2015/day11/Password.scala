package aoc2015.day11

import aoc2015.util.AocError

/** A corporate password: exactly eight lowercase letters.
  *
  * The opaque type guarantees that any `Password` value has already passed the length and alphabet
  * checks, so the part functions never re-validate those invariants.
  */
opaque type Password = Vector[Char]

object Password:

  private val Length = 8

  /** Validate `input` as a password of exactly eight lowercase letters, ignoring surrounding
    * whitespace.
    *
    * @param filename
    *   the resource name, used for error messages.
    * @param input
    *   the raw input.
    * @return
    *   the parsed password, or a validation error describing the first violation.
    */
  def parse(filename: String, input: String): Either[AocError, Password] =
    val trimmed = input.trim
    if trimmed.length != Length then
      Left(AocError.ValidationError(s"$filename: expected $Length lowercase letters"))
    else
      trimmed.toVector.zipWithIndex.find { case (character, _) =>
        character < 'a' || character > 'z'
      } match
        case Some((character, index)) => Left(
            AocError.ValidationError(s"$filename: position ${index +
                1}: unexpected character '$character'")
          )
        case None => Right(trimmed.toVector)

  end parse

  /** The password as its eight-letter string form. */
  def show(password: Password): String = password.mkString

end Password

/** Advance a password to the next candidate by incrementing its rightmost letter, wrapping `z`
  * around to `a` and carrying left.
  */
private def increment(password: Password): Password =
  def step(index: Int, letters: Vector[Char]): Vector[Char] =
    if letters(index) == 'z' then step(index - 1, letters.updated(index, 'a'))
    else letters.updated(index, (letters(index) + 1).toChar)

  if password.forall(_ == 'z') then password else step(password.length - 1, password)

end increment

/** True when `password` contains an increasing straight of at least three letters such as `abc` or
  * `bcd`.
  */
private def hasStraight(password: Password): Boolean = password.sliding(3)
  .exists(window => window(1) == window(0) + 1 && window(2) == window(1) + 1)

/** True when `password` contains none of the ambiguous letters `i`, `o`, or `l`. */
private def hasNoAmbiguousLetters(password: Password): Boolean = !password
  .exists(letter => letter == 'i' || letter == 'o' || letter == 'l')

/** True when `password` contains at least two different, non-overlapping pairs such as `aa` and
  * `bb`.
  */
private def hasTwoPairs(password: Password): Boolean =
  val pairs = password.sliding(2).collect { case Vector(a, b) if a == b => a }
  pairs.distinct.size >= 2

/** The next valid corporate password strictly after `password`.
  *
  * Starting from `password`, increment repeatedly until a candidate satisfies all three rules at
  * once: it contains an increasing straight of three letters, avoids `i`/`o`/`l`, and contains two
  * different non-overlapping pairs. Counting upward reaches a valid candidate deterministically.
  */
def nextPassword(password: Password): Password =
  @scala.annotation.tailrec
  def loop(candidate: Password): Password =
    if hasStraight(candidate) && hasNoAmbiguousLetters(candidate) && hasTwoPairs(candidate) then
      candidate
    else loop(increment(candidate))

  loop(increment(password))

end nextPassword

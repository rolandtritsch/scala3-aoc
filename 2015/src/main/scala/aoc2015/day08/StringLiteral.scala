package aoc2015.day08

import aoc2015.util.AocError

/** One double-quoted string literal from Santa's list, together with its decoded value.
  *
  * `code` is the literal exactly as it appears in the file (including the surrounding double
  * quotes); `value` is the in-memory string after resolving the escape sequences `\\`, `\"`, and
  * `\xHH`. The puzzle's answer compares the character counts of these two representations.
  *
  * @param code
  *   the raw literal text, including its surrounding quotes.
  * @param value
  *   the decoded in-memory string.
  */
final case class StringLiteral private (code: String, value: String):

  /** The number of characters of string code for this literal (its raw length). */
  def codeLength: Int = code.length

  /** The number of characters in memory for this literal's value. */
  def memoryLength: Int = value.length
end StringLiteral

object StringLiteral:

  private def isHex(c: Char): Boolean =
    (c >= '0' && c <= '9') ||
      (c >= 'a' && c <= 'f') ||
      (c >= 'A' && c <= 'F')

  /** Decode the body of a literal into its in-memory string, resolving escapes.
    *
    * The only legal escapes are `\\` (a backslash), `\"` (a quote), and `\x` followed by two
    * hexadecimal digits (a single character with that code). Any other backslash sequence is an
    * error.
    *
    * @param filename
    *   the resource name, used for error messages.
    * @param body
    *   the literal text between the surrounding quotes.
    * @param lineNumber
    *   the one-based line number, used for error messages.
    * @return
    *   the decoded string, or a parse error describing the invalid escape.
    */
  private def decode(filename: String, body: String, lineNumber: Int): Either[AocError, String] =
    @scala.annotation.tailrec
    def loop(index: Int, builder: StringBuilder): Either[AocError, String] =
      if index >= body.length then Right(builder.toString)
      else if body.charAt(index) != '\\' then loop(index + 1, builder.append(body.charAt(index)))
      else if index + 1 >= body.length then
        Left(AocError.ParseError(filename, s"line $lineNumber: trailing backslash"))
      else
        body.charAt(index + 1) match
          case '\\' => loop(index + 2, builder.append('\\'))
          case '"'  => loop(index + 2, builder.append('"'))
          case 'x'  =>
            if index + 3 >= body.length || !isHex(body.charAt(index + 2)) ||
              !isHex(body.charAt(index + 3))
            then Left(AocError.ParseError(filename, s"line $lineNumber: invalid \\x escape"))
            else
              val code = Integer.parseInt(body.substring(index + 2, index + 4), 16)
              loop(index + 4, builder.append(code.toChar))
          case other =>
            Left(AocError.ParseError(filename, s"line $lineNumber: invalid escape \\\\$other"))
    end loop

    loop(0, new StringBuilder)
  end decode

  /** Parse one line of input as a double-quoted string literal.
    *
    * The line must begin and end with a double quote and contain only valid escape sequences.
    *
    * @param filename
    *   the resource name, used for error messages.
    * @param line
    *   the raw literal text as it appears in the file.
    * @param lineNumber
    *   the one-based line number, used for error messages.
    * @return
    *   the parsed literal, or a parse error.
    */
  def parse(filename: String, line: String, lineNumber: Int = 1): Either[AocError, StringLiteral] =
    if line.length < 2 || line.head != '"' || line.last != '"' then
      Left(AocError.ParseError(filename, s"line $lineNumber: expected a quoted string literal"))
    else
      decode(filename, line.substring(1, line.length - 1), lineNumber).map(StringLiteral(line, _))

end StringLiteral

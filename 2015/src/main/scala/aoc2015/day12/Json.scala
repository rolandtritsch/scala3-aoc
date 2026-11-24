package aoc2015.day12

import aoc2015.util.AocError

/** A JSON value.
  *
  * The accounting document is a JSON document built from arrays, objects, numbers, strings,
  * booleans, and null. Numbers are integers (the puzzle guarantees no fractional values), so the
  * numeric case stores a `Long`.
  */
enum Json:
  case Null
  case Bool(value: Boolean)
  case Number(value: Long)
  case Text(value: String)
  case Array(elements: Seq[Json])
  case Object(entries: Seq[(String, Json)])
end Json

object Json:

  /** Parse `input` as a single JSON value, ignoring surrounding whitespace.
    *
    * @param filename
    *   the resource name, used for error messages.
    * @param input
    *   the raw text of the document.
    * @return
    *   the parsed value, or a parse error reporting the approximate character position.
    */
  def parse(filename: String, input: String): Either[AocError, Json] =
    val start = skipWhitespace(input, 0)
    if start >= input.length then Left(failure(filename, start, "expected a JSON value"))
    else
      parseValue(filename, input, start).flatMap { case (value, end) =>
        val trailing = skipWhitespace(input, end)
        if trailing == input.length then Right(value)
        else Left(failure(filename, trailing, "trailing content"))
      }

  end parse

  private def failure(filename: String, position: Int, message: String): AocError = AocError
    .ParseError(filename, s"position ${position + 1}: $message")

  @scala.annotation.tailrec
  private def skipWhitespace(input: String, position: Int): Int =
    if position < input.length && input.charAt(position).isWhitespace then
      skipWhitespace(input, position + 1)
    else position

  private def parseValue(
      filename: String,
      input: String,
      position: Int,
  ): Either[AocError, (Json, Int)] =
    if position >= input.length then Left(failure(filename, position, "unexpected end of input"))
    else
      input.charAt(position) match
        case 'n' => parseKeyword(filename, input, position, "null", Json.Null)
        case 't' => parseKeyword(filename, input, position, "true", Json.Bool(value = true))
        case 'f' => parseKeyword(filename, input, position, "false", Json.Bool(value = false))
        case '"' => parseString(filename, input, position).map { case (text, after) =>
            (Json.Text(text), after)
          }
        case '[' => parseArray(filename, input, position)
        case '{' => parseObject(filename, input, position)
        case character if character == '-' || character.isDigit =>
          parseNumber(filename, input, position)
        case character => Left(failure(filename, position, s"unexpected character '$character'"))

  end parseValue

  private def parseKeyword(
      filename: String,
      input: String,
      start: Int,
      keyword: String,
      value: Json,
  ): Either[AocError, (Json, Int)] =
    val end = start + keyword.length
    if end <= input.length && input.substring(start, end) == keyword then Right((value, end))
    else Left(failure(filename, start, s"expected '$keyword'"))

  end parseKeyword

  private def parseString(
      filename: String,
      input: String,
      start: Int,
  ): Either[AocError, (String, Int)] =
    def loop(position: Int, builder: StringBuilder): Either[AocError, (String, Int)] =
      if position >= input.length then Left(failure(filename, position, "unterminated string"))
      else
        input.charAt(position) match
          case '"'  => Right((builder.result(), position + 1))
          case '\\' => decodeEscape(filename, input, position + 1)
              .flatMap { case (character, next) => loop(next, builder.append(character)) }
          case character => loop(position + 1, builder.append(character))

    end loop

    loop(start + 1, StringBuilder())

  end parseString

  private def decodeEscape(
      filename: String,
      input: String,
      start: Int,
  ): Either[AocError, (Char, Int)] =
    if start >= input.length then Left(failure(filename, start, "unterminated escape"))
    else
      input.charAt(start) match
        case '"'       => Right(('"', start + 1))
        case '\\'      => Right(('\\', start + 1))
        case '/'       => Right(('/', start + 1))
        case 'b'       => Right(('\b', start + 1))
        case 'f'       => Right(('\f', start + 1))
        case 'n'       => Right(('\n', start + 1))
        case 'r'       => Right(('\r', start + 1))
        case 't'       => Right(('\t', start + 1))
        case 'u'       => decodeUnicode(filename, input, start + 1)
        case character => Left(failure(filename, start, s"invalid escape '\\$character'"))

  end decodeEscape

  private def decodeUnicode(
      filename: String,
      input: String,
      start: Int,
  ): Either[AocError, (Char, Int)] =
    val end = start + 4
    if end > input.length then Left(failure(filename, start, "incomplete unicode escape"))
    else
      val hex = input.substring(start, end)
      if hex.forall(isHexDigit) then Right((Integer.parseInt(hex, 16).toChar, end))
      else Left(failure(filename, start, s"invalid unicode escape '\\u$hex'"))

  end decodeUnicode

  private def isHexDigit(character: Char): Boolean = character.isDigit ||
    character >= 'a' && character <= 'f' || character >= 'A' && character <= 'F'

  private def parseNumber(
      filename: String,
      input: String,
      start: Int,
  ): Either[AocError, (Json, Int)] =
    val afterSign = if input.charAt(start) == '-' then start + 1 else start
    val end = scanDigits(input, afterSign)
    if end == afterSign then Left(failure(filename, start, "expected a number"))
    else if end < input.length && isNumberContinuation(input.charAt(end)) then
      Left(failure(filename, start, "non-integer numbers are not supported"))
    else
      input.substring(start, end).toLongOption match
        case Some(value) => Right((Json.Number(value), end))
        case None        => Left(failure(filename, start, "number out of range"))

  end parseNumber

  private def isNumberContinuation(character: Char): Boolean = character == '.' ||
    character == 'e' || character == 'E'

  @scala.annotation.tailrec
  private def scanDigits(input: String, position: Int): Int =
    if position < input.length && input.charAt(position).isDigit then
      scanDigits(input, position + 1)
    else position

  private def parseArray(
      filename: String,
      input: String,
      start: Int,
  ): Either[AocError, (Json, Int)] =
    def loop(position: Int, reversed: List[Json]): Either[AocError, (Json, Int)] =
      val next = skipWhitespace(input, position)
      if next >= input.length then Left(failure(filename, next, "unterminated array"))
      else if input.charAt(next) == ']' then Right((Json.Array(reversed.reverse), next + 1))
      else
        parseValue(filename, input, next).flatMap { case (value, afterValue) =>
          val after = skipWhitespace(input, afterValue)
          if after >= input.length then Left(failure(filename, after, "unterminated array"))
          else
            input.charAt(after) match
              case ','       => loop(after + 1, value :: reversed)
              case ']'       => Right((Json.Array((value :: reversed).reverse), after + 1))
              case character =>
                Left(failure(filename, after, s"expected ',' or ']', found '$character'"))
        }

    end loop

    loop(start + 1, Nil)

  end parseArray

  private def parseObject(
      filename: String,
      input: String,
      start: Int,
  ): Either[AocError, (Json, Int)] =
    def loop(position: Int, reversed: List[(String, Json)]): Either[AocError, (Json, Int)] =
      val next = skipWhitespace(input, position)
      if next >= input.length then Left(failure(filename, next, "unterminated object"))
      else if input.charAt(next) == '}' then Right((Json.Object(reversed.reverse), next + 1))
      else
        parseString(filename, input, next).flatMap { case (key, afterKey) =>
          val afterColon = skipWhitespace(input, afterKey)
          if afterColon >= input.length || input.charAt(afterColon) != ':' then
            Left(failure(filename, afterColon, "expected ':' after object key"))
          else
            val valueStart = skipWhitespace(input, afterColon + 1)
            parseValue(filename, input, valueStart).flatMap { case (value, afterValue) =>
              val after = skipWhitespace(input, afterValue)
              if after >= input.length then Left(failure(filename, after, "unterminated object"))
              else
                input.charAt(after) match
                  case ',' => loop(after + 1, (key, value) :: reversed)
                  case '}' => Right((Json.Object(((key, value) :: reversed).reverse), after + 1))
                  case character =>
                    Left(failure(filename, after, s"expected ',' or '}', found '$character'"))
            }
        }

    end loop

    loop(start + 1, Nil)

  end parseObject

end Json

package aoc2015.day08

import aoc2015.util.AocError
import zio.test.*

object Day08Test extends ZIOSpecDefault:

  def spec: Spec[Any, Any] = suite("Day08")(
    test("parse - empty and plain strings decode"):
      assertTrue(
        StringLiteral.parse("sample", "\"\"").map(l => (l.code, l.value)) == Right(("\"\"", "")),
        StringLiteral.parse("sample", "\"abc\"").map(l => (l.code, l.value)) ==
          Right(("\"abc\"", "abc")),
      )
    ,
    test("parse - escaped quote and backslash decode"):
      assertTrue(
        StringLiteral.parse("sample", "\"aaa\\\"aaa\"").map(l => (l.code, l.value)) ==
          Right(("\"aaa\\\"aaa\"", "aaa\"aaa")),
        StringLiteral.parse("sample", "\"a\\\\b\"").map(l => (l.code, l.value)) ==
          Right(("\"a\\\\b\"", "a\\b")),
      )
    ,
    test("parse - hex escape decodes to a single character"):
      assertTrue(
        StringLiteral.parse("sample", "\"\\x27\"").map(l => (l.code, l.value)) ==
          Right(("\"\\x27\"", "\u0027")),
        StringLiteral.parse("sample", "\"\\x41\\x42\"").map(l => (l.code, l.value)) ==
          Right(("\"\\x41\\x42\"", "AB")),
      )
    ,
    test("parse - rejects malformed literals"):
      assertTrue(
        Seq("abc\"", "\"abc", "abc", "\"\\q\"", "\"\\x2\"", "\"abc\\\"")
          .forall(line => StringLiteral.parse("sample", line).isLeft)
      )
    ,
    test("part1 - official sample difference of 12"):
      readFile("inputs/Day08Boundary.txt").map(literals => assertTrue(part1(literals) == 12))
    ,
    test("part1 - real input answer"):
      readFile("inputs/Day08.txt").map(literals => assertTrue(part1(literals) == 1350))
    ,
    test("readFile - malformed escape reports the offending line"):
      readFile("inputs/Day08Invalid.txt").either.map(result =>
        assertTrue(result.left.exists {
          case AocError.ParseError("inputs/Day08Invalid.txt", message) => message.contains("line 2")
          case _                                                       => false
        })
      )
    ,
    test("readFile - missing surrounding quote rejected"):
      readFile("inputs/Day08InvalidQuote.txt").either.map(result =>
        assertTrue(result.left.exists {
          case AocError.ParseError("inputs/Day08InvalidQuote.txt", message) => message
              .contains("line 1")
          case _ => false
        })
      )
    ,
    test("readFile - real input loads as string literals"):
      readFile("inputs/Day08.txt").map(literals => assertTrue(literals.nonEmpty)),
  )
  end spec

end Day08Test

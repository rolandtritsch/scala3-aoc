package aoc2015.day12

import aoc2015.util.AocError
import zio.test.*

object Day12Test extends ZIOSpecDefault:

  def spec: Spec[Any, Any] = suite("Day12")(
    test("parse - accepts every JSON value shape and surrounding whitespace"):
      assertTrue(
        Json.parse("sample", "[1,2,3]").isRight,
        Json.parse("sample", "{\"a\":2,\"b\":4}").isRight,
        Json.parse("sample", "  \"text\"\n").isRight,
        Json.parse("sample", "true").isRight,
        Json.parse("sample", "null").isRight,
        Json.parse("sample", "[-1,{\"a\":1}]") == Json.parse("sample", " [-1,{\"a\":1}]"),
      )
    ,
    test("parse - rejects malformed JSON"):
      assertTrue(
        Seq("", "[1,2,", "{\"a\": }", "tru", "nul", "[1 2]", "{\"a\" 1}")
          .forall(input => Json.parse("sample", input).isLeft)
      )
    ,
    test("part1 - official examples"):
      assertTrue(
        Seq(
          "[1,2,3]" -> 6L,
          "{\"a\":2,\"b\":4}" -> 6L,
          "[[[3]]]" -> 3L,
          "{\"a\":{\"b\":4},\"c\":-1}" -> 3L,
          "{\"a\":[-1,1]}" -> 0L,
          "[-1,{\"a\":1}]" -> 0L,
          "[]" -> 0L,
          "{}" -> 0L,
        ).forall { case (input, expected) =>
          Json.parse("sample", input).map(part1) == Right(expected)
        }
      )
    ,
    test("part1 - real input answer"):
      readFile("inputs/Day12.txt").map(json => assertTrue(part1(json) == 191164L))
    ,
    test("part1 - boundary input answer"):
      readFile("inputs/Day12Boundary.txt").map(json => assertTrue(part1(json) == 35L))
    ,
    test("part2 - official examples"):
      assertTrue(
        Seq(
          "[1,2,3]" -> 6L,
          "[1,{\"c\":\"red\",\"b\":2},3]" -> 4L,
          "{\"d\":\"red\",\"e\":[1,2,3,4],\"f\":5}" -> 0L,
          "[1,\"red\",5]" -> 6L,
        ).forall { case (input, expected) =>
          Json.parse("sample", input).map(part2) == Right(expected)
        }
      )
    ,
    test("part2 - boundary input answer"):
      readFile("inputs/Day12Boundary.txt").map(json => assertTrue(part2(json) == 35L))
    ,
    test("part2 - real input answer"):
      readFile("inputs/Day12.txt").map(json => assertTrue(part2(json) == 87842L))
    ,
    test("readFile - malformed document reports its position"):
      readFile("inputs/Day12Invalid.txt").either.map(result =>
        assertTrue(result.left.exists {
          case AocError.ParseError(_, message) => message.contains("position")
          case _                               => false
        })
      )
    ,
    test("readFile - real input loads as a non-empty top-level array"):
      readFile("inputs/Day12.txt").map {
        case Json.Array(elements) => assertTrue(elements.nonEmpty)
        case _                    => assertTrue(false)
      },
  )
  end spec

end Day12Test

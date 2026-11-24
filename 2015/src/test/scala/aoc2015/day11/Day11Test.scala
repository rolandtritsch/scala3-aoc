package aoc2015.day11

import aoc2015.util.AocError
import zio.test.*

object Day11Test extends ZIOSpecDefault:

  def spec: Spec[Any, Any] = suite("Day11")(
    test("parse - accepts exactly eight lowercase letters and surrounding whitespace"):
      assertTrue(
        Password.parse("sample", "cqjxjnds") == Password.parse("sample", " cqjxjnds\n"),
        Password.parse("sample", "cqjxjnds").isRight,
      )
    ,
    test("parse - rejects wrong length and non-lowercase input"):
      assertTrue(
        Seq("", "abc", "abcdefghi", "abcdefgH", "abcdefg1", "abcdefgh i")
          .forall(input => Password.parse("sample", input).isLeft)
      )
    ,
    test("part1 - official examples"):
      assertTrue(
        part1(Password.parse("sample", "abcdefgh").toOption.get) == "abcdffaa",
        part1(Password.parse("sample", "ghijklmn").toOption.get) == "ghjaabcc",
      )
    ,
    test("part1 - real input answer"):
      readFile("inputs/Day11.txt").map(password => assertTrue(part1(password) == "cqjxxyzz"))
    ,
    test("part1 - boundary input answer"):
      readFile("inputs/Day11Boundary.txt")
        .map(password => assertTrue(part1(password) == "abcdffaa"))
    ,
    test("part2 - official examples"):
      assertTrue(
        part2(Password.parse("sample", "abcdefgh").toOption.get) == "abcdffbb",
        part2(Password.parse("sample", "ghijklmn").toOption.get) == "ghjbbcdd",
      )
    ,
    test("part2 - real input answer"):
      readFile("inputs/Day11.txt").map(password => assertTrue(part2(password) == "cqkaabcc"))
    ,
    test("part2 - boundary input answer"):
      readFile("inputs/Day11Boundary.txt")
        .map(password => assertTrue(part2(password) == "abcdffbb"))
    ,
    test("readFile - non-lowercase input reports its position"):
      readFile("inputs/Day11Invalid.txt").either.map(result =>
        assertTrue(result.left.exists {
          case AocError.ValidationError(message) => message.contains("position 1")
          case _                                 => false
        })
      )
    ,
    test("readFile - real input loads as eight lowercase letters"):
      readFile("inputs/Day11.txt")
        .map(password => assertTrue(Password.show(password) == "cqjxjnds")),
  )
  end spec

end Day11Test

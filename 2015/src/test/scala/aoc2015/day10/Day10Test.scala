package aoc2015.day10

import aoc2015.util.AocError
import zio.test.*

object Day10Test extends ZIOSpecDefault:

  def spec: Spec[Any, Any] = suite("Day10")(
    test("parse - accepts decimal digits and surrounding whitespace"):
      assertTrue(
        Digits.parse("sample", "1321131112") == Digits.parse("sample", " 1321131112\n"),
        Digits.parse("sample", "1321131112").isRight,
      )
    ,
    test("parse - rejects empty, non-digit, and internally separated input"):
      assertTrue(
        Seq("", " \n\t", "12a3", "12 3", "12\n3")
          .forall(input => Digits.parse("sample", input).isLeft)
      )
    ,
    test("lookAndSay - official step examples"):
      assertTrue(
        Seq("1", "11", "21", "1211", "111221").zip(Seq("11", "21", "1211", "111221", "312211"))
          .forall { (before, after) =>
            Digits.parse("sample", before).map(lookAndSay) == Digits.parse("sample", after)
          }
      )
    ,
    test("lookAndSay - handles runs longer than nine digits"):
      assertTrue(
        Digits.parse("sample", "1111111111").map(lookAndSay) == Digits.parse("sample", "101")
      )
    ,
    test("part1 - real input answer"):
      readFile("inputs/Day10.txt").map(digits => assertTrue(part1(digits) == 492982))
    ,
    test("part1 - single digit input length after 40 steps"):
      readFile("inputs/Day10Boundary.txt").map(digits => assertTrue(part1(digits) == 82350))
    ,
    test("part2 - real input answer"):
      readFile("inputs/Day10.txt").map(digits => assertTrue(part2(digits) == 6989950))
    ,
    test("part2 - single digit input length after 50 steps"):
      readFile("inputs/Day10Boundary.txt").map(digits => assertTrue(part2(digits) == 1166642))
    ,
    test("readFile - malformed digit reports its position"):
      readFile("inputs/Day10Invalid.txt").either.map(result =>
        assertTrue(result.left.exists {
          case AocError.ParseError("inputs/Day10Invalid.txt", message) => message
              .contains("position 5")
          case _ => false
        })
      )
    ,
    test("readFile - real input loads as a non-empty sequence"):
      readFile("inputs/Day10.txt").map(digits => assertTrue(Digits.length(digits) > 0)),
  )
  end spec

end Day10Test

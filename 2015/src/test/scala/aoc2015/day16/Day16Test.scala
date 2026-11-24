package aoc2015.day16

import aoc2015.util.AocError
import zio.test.*

object Day16Test extends ZIOSpecDefault:

  def spec: Spec[Any, Any] = suite("Day16")(
    test("parse - a full Sue with three remembered facts"):
      assertTrue(
        AuntSue.parse("sample", "Sue 1: cars: 9, akitas: 3, goldfish: 0")
          .map(s => (s.number, s.remembered)) ==
          Right((1, Map(Attribute.Cars -> 9, Attribute.Akitas -> 3, Attribute.Goldfish -> 0)))
      )
    ,
    test("parse - rejects malformed lines"):
      assertTrue(
        Seq(
          "Sue 1 cars: 9, akitas: 3",
          "Sue 0: cars: 9",
          "Sue 1: cars: nine",
          "Sue 1: cars: 9, cars: 3",
          "Sue 1: unknown: 9",
          "Sue 1: cars 9",
        ).forall(line => AuntSue.parse("sample", line).isLeft)
      )
    ,
    test("consistentWithTape - remembered facts must match exactly"):
      assertTrue(
        consistentWithTape(AuntSue(1, Map(Attribute.Children -> 3, Attribute.Cats -> 7))),
        !consistentWithTape(AuntSue(2, Map(Attribute.Children -> 4))),
        consistentWithTape(AuntSue(3, Map(Attribute.Akitas -> 0, Attribute.Vizslas -> 0))),
      )
    ,
    test("consistentWithRanges - ranges override exact matches for four attributes"):
      assertTrue(
        consistentWithRanges(AuntSue(1, Map(Attribute.Cats -> 8, Attribute.Trees -> 4))),
        consistentWithRanges(AuntSue(2, Map(Attribute.Pomeranians -> 2, Attribute.Goldfish -> 4))),
        consistentWithRanges(AuntSue(3, Map(Attribute.Children -> 3, Attribute.Cars -> 2))),
        !consistentWithRanges(AuntSue(4, Map(Attribute.Cats -> 7))),
        !consistentWithRanges(AuntSue(5, Map(Attribute.Trees -> 3))),
        !consistentWithRanges(AuntSue(6, Map(Attribute.Pomeranians -> 3))),
        !consistentWithRanges(AuntSue(7, Map(Attribute.Goldfish -> 5))),
        !consistentWithRanges(AuntSue(8, Map(Attribute.Children -> 4))),
      )
    ,
    test("part2 - boundary input identifies Sue 4"):
      readFile("inputs/Day16Boundary.txt").map(sues => assertTrue(part2(sues) == Right(4)))
    ,
    test("part2 - real input answer"):
      readFile("inputs/Day16.txt").map(sues => assertTrue(part2(sues) == Right(260)))
    ,
    test("part1 - boundary input identifies Sue 1"):
      readFile("inputs/Day16Boundary.txt").map(sues => assertTrue(part1(sues) == Right(1)))
    ,
    test("part1 - real input answer"):
      readFile("inputs/Day16.txt").map(sues => assertTrue(part1(sues) == Right(373)))
    ,
    test("readFile - malformed line reports the offending line"):
      readFile("inputs/Day16Invalid.txt").either.map(result =>
        assertTrue(result.left.exists {
          case AocError.ParseError("inputs/Day16Invalid.txt", message) => message.contains("line 2")
          case _                                                       => false
        })
      )
    ,
    test("readFile - real input loads as Sues"):
      readFile("inputs/Day16.txt").map(sues => assertTrue(sues.nonEmpty)),
  )
  end spec

end Day16Test

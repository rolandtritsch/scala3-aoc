package aoc2015.day13

import aoc2015.util.AocError
import zio.test.*

object Day13Test extends ZIOSpecDefault:

  def spec: Spec[Any, Any] = suite("Day13")(
    test("parse - a gain and a lose preference"):
      assertTrue(
        Preference.parse("sample", "Alice would gain 54 happiness units by sitting next to Bob.")
          .map(p => (p.person, p.neighbor, p.change)) == Right(("Alice", "Bob", 54)),
        Preference.parse("sample", "Alice would lose 79 happiness units by sitting next to Carol.")
          .map(p => (p.person, p.neighbor, p.change)) == Right(("Alice", "Carol", -79)),
      )
    ,
    test("parse - rejects malformed lines"):
      assertTrue(
        Seq(
          "Alice would gain 54 happiness units by sitting next to Bob",
          "Alice would 54 happiness units by sitting next to Bob.",
          "Alice would gain happiness units by sitting next to Bob.",
          "Alice would gain -5 happiness units by sitting next to Bob.",
          "Alice would gain abc happiness units by sitting next to Bob.",
          "Alice would gain 5 happiness units by sitting next to Alice.",
          "Alice would lose 5 happiness units by sitting next to Alice.",
        ).forall(line => Preference.parse("sample", line).isLeft)
      )
    ,
    test("part1 - official sample optimal seating of 330"):
      readFile("inputs/Day13Boundary.txt").map(preferences => assertTrue(part1(preferences) == 330))
    ,
    test("part1 - real input answer"):
      readFile("inputs/Day13.txt").map(preferences => assertTrue(part1(preferences) == 709))
    ,
    test("readFile - malformed line reports the offending line"):
      readFile("inputs/Day13Invalid.txt").either.map(result =>
        assertTrue(result.left.exists {
          case AocError.ParseError("inputs/Day13Invalid.txt", message) => message.contains("line 2")
          case _                                                       => false
        })
      )
    ,
    test("readFile - real input loads as preferences"):
      readFile("inputs/Day13.txt").map(preferences => assertTrue(preferences.nonEmpty)),
  )
  end spec

end Day13Test

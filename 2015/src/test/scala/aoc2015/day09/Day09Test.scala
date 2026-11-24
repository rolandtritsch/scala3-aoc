package aoc2015.day09

import aoc2015.util.AocError
import zio.test.*

object Day09Test extends ZIOSpecDefault:

  def spec: Spec[Any, Any] = suite("Day09")(
    test("parse - a single distance"):
      assertTrue(
        Distance.parse("sample", "London to Dublin = 464").map(d => (d.from, d.to, d.distance)) ==
          Right(("London", "Dublin", 464))
      )
    ,
    test("parse - rejects malformed lines"):
      assertTrue(
        Seq(
          "London Dublin = 464",
          "London to Dublin 464",
          "London to Dublin = -1",
          "London to Dublin = 0",
          "London to London = 5",
          "London to Dublin = abc",
        ).forall(line => Distance.parse("sample", line).isLeft)
      )
    ,
    test("part1 - official sample shortest route of 605"):
      readFile("inputs/Day09Boundary.txt").map(distances => assertTrue(part1(distances) == 605))
    ,
    test("part1 - real input answer"):
      readFile("inputs/Day09.txt").map(distances => assertTrue(part1(distances) == 141))
    ,
    test("part2 - official sample longest route of 982"):
      readFile("inputs/Day09Boundary.txt").map(distances => assertTrue(part2(distances) == 982))
    ,
    test("part2 - real input answer"):
      readFile("inputs/Day09.txt").map(distances => assertTrue(part2(distances) == 736))
    ,
    test("readFile - malformed line reports the offending line"):
      readFile("inputs/Day09Invalid.txt").either.map(result =>
        assertTrue(result.left.exists {
          case AocError.ParseError("inputs/Day09Invalid.txt", message) => message.contains("line 2")
          case _                                                       => false
        })
      )
    ,
    test("readFile - real input loads as distances"):
      readFile("inputs/Day09.txt").map(distances => assertTrue(distances.nonEmpty)),
  )
  end spec

end Day09Test

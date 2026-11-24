package aoc2015.day14

import scala.compiletime.testing.typeCheckErrors

import aoc2015.util.AocError
import zio.test.*

object Day14Test extends ZIOSpecDefault:

  def spec: Spec[Any, Any] = suite("Day14")(
    test("domain values cannot bypass validation through copy"):
      assertTrue(
        typeCheckErrors(
          """aoc2015.day14.Reindeer.parse("sample", "Comet can fly 14 km/s for 10 seconds, but then must rest for 127 seconds.").map(_.name)"""
        ).isEmpty,
        typeCheckErrors(
          """aoc2015.day14.Reindeer.parse("sample", "Comet can fly 14 km/s for 10 seconds, but then must rest for 127 seconds.").map(_.copy(speed = 0))"""
        ).nonEmpty,
      )
    ,
    test("parse - a one-second flyer"):
      assertTrue(
        Reindeer.parse(
          "sample",
          "Dancer can fly 37 km/s for 1 seconds, but then must rest for 36 seconds.",
        ).map(r => (r.name, r.speed, r.flySeconds, r.restSeconds)) == Right(("Dancer", 37, 1, 36))
      )
    ,
    test("parse - rejects malformed lines"):
      assertTrue(
        Seq(
          "Comet can fly 14 km/s for 10 seconds, but then must rest for 127 seconds",
          "Comet can fly fast for 10 seconds, but then must rest for 127 seconds.",
          "Comet can fly 14 km/s, but then must rest for 127 seconds.",
          "Comet can fly 0 km/s for 10 seconds, but then must rest for 127 seconds.",
          "Comet can fly 14 km/s for 0 seconds, but then must rest for 127 seconds.",
          "Comet can fly 14 km/s for 10 seconds, but then must rest for 0 seconds.",
          "Comet can fly -5 km/s for 10 seconds, but then must rest for 127 seconds.",
        ).forall(line => Reindeer.parse("sample", line).isLeft)
      )
    ,
    test("distanceAfter - official sample at 1000 seconds"):
      assertTrue(
        Reindeer.parse(
          "sample",
          "Comet can fly 14 km/s for 10 seconds, but then must rest for 127 seconds.",
        ).map(distanceAfter(_, 1000)) == Right(1120),
        Reindeer.parse(
          "sample",
          "Dancer can fly 16 km/s for 11 seconds, but then must rest for 162 seconds.",
        ).map(distanceAfter(_, 1000)) == Right(1056),
      )
    ,
    test("part1 - boundary sample at 2503 seconds"):
      readFile("inputs/Day14Boundary.txt").map(reindeer => assertTrue(part1(reindeer) == 2660))
    ,
    test("part1 - real input answer"):
      readFile("inputs/Day14.txt").map(reindeer => assertTrue(part1(reindeer) == 2655))
    ,
    test("readFile - malformed line reports the offending line"):
      readFile("inputs/Day14Invalid.txt").either.map(result =>
        assertTrue(result.left.exists {
          case AocError.ParseError("inputs/Day14Invalid.txt", message) => message.contains("line 2")
          case _                                                       => false
        })
      )
    ,
    test("readFile - real input loads as reindeer"):
      readFile("inputs/Day14.txt").map(reindeer => assertTrue(reindeer.nonEmpty)),
  )
  end spec

end Day14Test

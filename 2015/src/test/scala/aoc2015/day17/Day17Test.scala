package aoc2015.day17

import aoc2015.util.AocError
import zio.test.*

object Day17Test extends ZIOSpecDefault:

  def spec: Spec[Any, Any] = suite("Day17")(
    test("parse - a positive whole-liters capacity"):
      assertTrue(Liters.parse("sample", "43").map(Liters.value) == Right(43))
    ,
    test("parse - rejects malformed lines"):
      assertTrue(
        Seq("", "0", "-5", "ten", "12.5", "43 extra")
          .forall(line => Liters.parse("sample", line).isLeft)
      )
    ,
    test("countCombinations - official example holds 25 liters four ways"):
      readFile("inputs/Day17Boundary.txt")
        .map(capacities => assertTrue(countCombinations(capacities, 25) == 4))
    ,
    test("countCombinations - only the empty selection holds zero liters"):
      readFile("inputs/Day17Boundary.txt")
        .map(capacities => assertTrue(countCombinations(capacities, 0) == 1))
    ,
    test("part1 - real input answer"):
      readFile("inputs/Day17.txt").map(capacities => assertTrue(part1(capacities) == 1638))
    ,
    test("minimalCombinations - official example uses two containers three ways"):
      readFile("inputs/Day17Boundary.txt")
        .map(capacities => assertTrue(minimalCombinations(capacities, 25) == 3))
    ,
    test("part2 - real input answer"):
      readFile("inputs/Day17.txt").map(capacities => assertTrue(part2(capacities) == 17))
    ,
    test("readFile - malformed line reports the offending line"):
      readFile("inputs/Day17Invalid.txt").either.map(result =>
        assertTrue(result.left.exists {
          case AocError.ParseError("inputs/Day17Invalid.txt", message) => message.contains("line 2")
          case _                                                       => false
        })
      )
    ,
    test("readFile - real input loads capacities"):
      readFile("inputs/Day17.txt").map(capacities => assertTrue(capacities.nonEmpty)),
  )
  end spec

end Day17Test

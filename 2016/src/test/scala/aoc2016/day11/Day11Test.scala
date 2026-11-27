package aoc2016.day11

import aoc2016.util.AocError
import zio.test.*

object Day11Test extends ZIOSpecDefault:

  private val officialExample =
    """The first floor contains a hydrogen-compatible microchip and a lithium-compatible microchip.
      |The second floor contains a hydrogen generator.
      |The third floor contains a lithium generator.
      |The fourth floor contains nothing relevant.""".stripMargin

  def spec: Spec[Any, Any] = suite("Day11")(
    test("parse - maps isotope pairs to zero-based floors"):
      assertTrue(
        parse("sample", officialExample) == Right(Facility(Seq(
          PairLocation(generatorFloor = 1, microchipFloor = 0),
          PairLocation(generatorFloor = 2, microchipFloor = 0),
        )))
      )
    ,
    test("parse - rejects malformed floors, items and incomplete pairs"):
      assertTrue(
        Seq(
          "",
          officialExample.replace("The second floor", "The third floor"),
          officialExample.replace("a hydrogen generator", "a hydrogen capacitor"),
          officialExample.replace("a lithium-compatible microchip", "a lithium generator"),
          officialExample.linesIterator.take(3).mkString("\n"),
          officialExample + "\nThe fifth floor contains nothing relevant.",
        ).forall(input => parse("sample", input).isLeft)
      )
    ,
    test("parse - reports the malformed floor line"):
      val malformed = officialExample.replace("The second floor", "The third floor")
      assertTrue(parse("sample", malformed).left.exists {
        case AocError.ParseError("sample", message) => message.contains("line 2")
        case _                                      => false
      })
    ,
    test("part1 - official example"):
      assertTrue(parse("sample", officialExample).map(part1) == Right(Some(11)))
    ,
    test("part1 - one separated pair takes three moves"):
      val input = Facility(Seq(PairLocation(generatorFloor = 1, microchipFloor = 0)))
      assertTrue(part1(input) == Some(3))
    ,
    test("part1 - an already assembled facility takes no moves"):
      val input = Facility(Seq(PairLocation(generatorFloor = 3, microchipFloor = 3)))
      assertTrue(part1(input) == Some(0))
    ,
    test("readFile and part1 - personal input"):
      readFile("inputs/Day11.txt").map(input => assertTrue(part1(input) == Some(37)))
    ,
    test("part2 - transports both additional pairs"):
      assertTrue(part2(Facility(Seq.empty)) == Some(15))
    ,
    test("readFile and part2 - personal input"):
      readFile("inputs/Day11.txt").map(input => assertTrue(part2(input) == Some(61)))
    ,
    test("readFile - maps every personal-input pair"):
      readFile("inputs/Day11.txt").map(input =>
        assertTrue(
          input == Facility(Seq(
            PairLocation(0, 0),
            PairLocation(0, 0),
            PairLocation(1, 1),
            PairLocation(1, 1),
            PairLocation(1, 2),
          ))
        )
      ),
  )
  end spec

end Day11Test

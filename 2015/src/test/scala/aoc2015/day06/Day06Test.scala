package aoc2015.day06

import scala.compiletime.testing.typeCheckErrors

import aoc2015.day06.Instruction.parse
import aoc2015.util.AocError
import aoc2015.util.Position
import zio.test.*

object Day06Test extends ZIOSpecDefault:

  private def parsed(lines: Seq[String]): Either[AocError, Seq[Instruction]] = lines
    .foldLeft[Either[AocError, Seq[Instruction]]](Right(Seq.empty)) { (result, line) =>
      for
        instructions <- result
        instruction <- parse("sample", line)
      yield instruction +: instructions
    }.map(_.reverse)
  end parsed

  private def solvePart1(lines: Seq[String]): Either[AocError, Int] = parsed(lines).map(part1)
  private def solvePart2(lines: Seq[String]): Either[AocError, Long] = parsed(lines).map(part2)

  def spec: Spec[Any, Any] = suite("Day06")(
    test("domain values cannot bypass validation through copy"):
      assertTrue(
        typeCheckErrors(
          """aoc2015.day06.Instruction.parse("sample", "turn on 0,0 through 1,1").map(_.to)"""
        ).isEmpty,
        typeCheckErrors(
          """aoc2015.day06.Instruction.parse("sample", "turn on 0,0 through 1,1").map(_.copy(to = aoc2015.util.Position(1000, 1000)))"""
        ).nonEmpty,
      )
    ,
    test("parse - rejects invalid bounds, reversed corners and oversized numbers"):
      assertTrue(
        Seq(
          "turn on -1,0 through 1,1",
          "turn on 0,0 through 1000,999",
          "toggle 2,0 through 1,1",
          "toggle 0,2 through 1,1",
          "turn off 0,0 through 999999999999999999999,1",
          "turn on 0,0 through 1,1 extra",
        ).forall(line => parse("sample", line).isLeft)
      )
    ,
    test("readFile - invalid coordinates fail before solving"):
      readFile("inputs/Day06Invalid.txt").either.map(result =>
        assertTrue(result.left.exists {
          case AocError.ParseError("inputs/Day06Invalid.txt", message) => message.contains("line 3")
          case _                                                       => false
        })
      )
    ,
    test("readFile - typed boundary cell and isolated evaluations"):
      readFile("inputs/Day06Boundary.txt").map(instructions =>
        assertTrue(
          part1(instructions) == 1,
          part2(instructions) == 2L,
          part1(instructions) == 1,
          part2(instructions) == 2L,
        )
      )
    ,
    test("part2 - official million-cell toggle"):
      assertTrue(solvePart2(Seq("toggle 0,0 through 999,999")) == Right(2000000L))
    ,
    test("parse - each action"):
      assertTrue(
        parse("sample", "turn on 0,0 through 999,999").map(i => (i.action, i.from, i.to)) ==
          Right((Action.TurnOn, Position(0, 0), Position(999, 999))),
        parse("sample", "turn off 499,499 through 500,500").map(i => (i.action, i.from, i.to)) ==
          Right((Action.TurnOff, Position(499, 499), Position(500, 500))),
        parse("sample", "toggle 0,0 through 999,0").map(i => (i.action, i.from, i.to)) ==
          Right((Action.Toggle, Position(0, 0), Position(999, 0))),
      )
    ,
    test("parse - multi-digit coordinates"):
      assertTrue(
        parse("sample", "toggle 322,558 through 977,958").map(i => (i.action, i.from, i.to)) ==
          Right((Action.Toggle, Position(322, 558), Position(977, 958))),
        parse("sample", "turn on 1,2 through 3,4").map(i => (i.action, i.from, i.to)) ==
          Right((Action.TurnOn, Position(1, 2), Position(3, 4))),
      )
    ,
    test("parse - rejects an unrecognized instruction"):
      assertTrue(parse("sample", "spin 0,0 through 1,1").isLeft)
    ,
    test("part1 - single cell"):
      assertTrue(
        solvePart1(Seq("turn on 0,0 through 0,0")) == Right(1),
        solvePart1(Seq("toggle 0,0 through 0,0")) == Right(1),
        solvePart1(Seq("turn off 0,0 through 0,0")) == Right(0),
      )
    ,
    test("part1 - official samples, applied in order"):
      val samples = Seq(
        "turn on 0,0 through 999,999",
        "toggle 0,0 through 999,0",
        "turn off 499,499 through 500,500",
      )
      assertTrue(
        solvePart1(Seq(samples.head)) == Right(1000000),
        solvePart1(samples.take(2)) == Right(999000),
        solvePart1(samples) == Right(998996),
      )
    ,
    test("part1 - empty input"):
      assertTrue(part1(Seq.empty) == 0)
    ,
    test("part1 - rectangle size"):
      assertTrue(
        solvePart1(Seq("turn on 0,0 through 2,2")) == Right(9),
        solvePart1(Seq("turn on 0,0 through 0,3")) == Right(4),
      )
    ,
    test("part1 - toggle twice restores state"):
      val instructions =
        Seq("turn on 0,0 through 9,9", "toggle 0,0 through 4,4", "toggle 0,0 through 4,4")
      assertTrue(solvePart1(instructions) == Right(100))
    ,
    test("readFile"):
      readFile("inputs/Day06.txt").map(obtained =>
        assertTrue(
          obtained.length == 300,
          obtained.headOption.exists(i =>
            i.action == Action.TurnOff && i.from == Position(660, 55) && i.to == Position(986, 197)
          ),
        )
      )
    ,
    test("part1 - real input"):
      readFile("inputs/Day06.txt").map(input => assertTrue(part1(input) == 400410))
    ,
    test("part2 - single cell brightness"):
      assertTrue(
        solvePart2(Seq("turn on 0,0 through 0,0")) == Right(1),
        solvePart2(Seq("toggle 0,0 through 0,0")) == Right(2),
        solvePart2(Seq("turn off 0,0 through 0,0")) == Right(0),
      )
    ,
    test("part2 - brightness never below zero"):
      val instructions = Seq("turn off 0,0 through 0,0", "turn off 0,0 through 0,0")
      assertTrue(solvePart2(instructions) == Right(0))
    ,
    test("part2 - rectangle sums"):
      val instructions = Seq("turn on 0,0 through 1,1", "toggle 0,0 through 0,1")
      assertTrue(solvePart2(instructions) == Right(4 + 4))
    ,
    test("part2 - empty input"):
      assertTrue(part2(Seq.empty) == 0)
    ,
    test("part2 - real input"):
      readFile("inputs/Day06.txt").map(input => assertTrue(part2(input) == 15343601L)),
  )
  end spec

end Day06Test

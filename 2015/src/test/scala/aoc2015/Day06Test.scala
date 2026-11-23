package aoc2015

import aoc2015.Day06.*
import aoc2015.Day06.Action
import zio.*
import zio.test.*

object Day06Test extends ZIOSpecDefault:

  def spec: Spec[Any, Any] = suite("Day06")(
    test("parse - each action"):
      assertTrue(
        parse("turn on 0,0 through 999,999") == Right((Action.TurnOn, 0, 0, 999, 999)),
        parse("turn off 499,499 through 500,500") == Right((Action.TurnOff, 499, 499, 500, 500)),
        parse("toggle 0,0 through 999,0") == Right((Action.Toggle, 0, 0, 999, 0)),
      )
    ,
    test("parse - multi-digit coordinates"):
      assertTrue(
        parse("toggle 322,558 through 977,958") == Right((Action.Toggle, 322, 558, 977, 958)),
        parse("turn on 1,2 through 3,4") == Right((Action.TurnOn, 1, 2, 3, 4)),
      )
    ,
    test("parse - rejects an unrecognized instruction"):
      assertTrue(parse("spin 0,0 through 1,1").isLeft)
    ,
    test("part1 - single cell"):
      assertTrue(
        part1(Seq("turn on 0,0 through 0,0")) == Right(1),
        part1(Seq("toggle 0,0 through 0,0")) == Right(1),
        part1(Seq("turn off 0,0 through 0,0")) == Right(0),
      )
    ,
    test("part1 - official samples, applied in order"):
      val samples = Seq(
        "turn on 0,0 through 999,999",
        "toggle 0,0 through 999,0",
        "turn off 499,499 through 500,500",
      )
      assertTrue(
        part1(Seq(samples.head)) == Right(1000000),
        part1(samples.take(2)) == Right(999000),
        part1(samples) == Right(998996),
      )
    ,
    test("part1 - empty input"):
      assertTrue(part1(Seq.empty) == Right(0))
    ,
    test("part1 - rectangle size"):
      assertTrue(
        part1(Seq("turn on 0,0 through 2,2")) == Right(9),
        part1(Seq("turn on 0,0 through 0,3")) == Right(4),
      )
    ,
    test("part1 - toggle twice restores state"):
      val instructions =
        Seq("turn on 0,0 through 9,9", "toggle 0,0 through 4,4", "toggle 0,0 through 4,4")
      assertTrue(part1(instructions) == Right(100))
    ,
    test("readFile"):
      readFile("inputs/Day06.txt").map(obtained =>
        assertTrue(obtained.length == 300, obtained.head == "turn off 660,55 through 986,197")
      )
    ,
    test("part1 - real input"):
      readFile("inputs/Day06.txt").map(input => assertTrue(part1(input).exists(_ > 0)))
    ,
    test("part2 - single cell brightness"):
      assertTrue(
        part2(Seq("turn on 0,0 through 0,0")) == Right(1),
        part2(Seq("toggle 0,0 through 0,0")) == Right(2),
        part2(Seq("turn off 0,0 through 0,0")) == Right(0),
      )
    ,
    test("part2 - brightness never below zero"):
      val instructions = Seq("turn off 0,0 through 0,0", "turn off 0,0 through 0,0")
      assertTrue(part2(instructions) == Right(0))
    ,
    test("part2 - rectangle sums"):
      val instructions = Seq("turn on 0,0 through 1,1", "toggle 0,0 through 0,1")
      assertTrue(part2(instructions) == Right(4 + 4))
    ,
    test("part2 - empty input"):
      assertTrue(part2(Seq.empty) == Right(0))
    ,
    test("part2 - real input"):
      readFile("inputs/Day06.txt").map(input => assertTrue(part2(input).exists(_ > 0))),
  )

end Day06Test

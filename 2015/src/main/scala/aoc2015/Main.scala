package aoc2015

import aoc2015.util.AocError
import zio.*

object Main extends ZIOAppDefault:

  private val program: ZIO[Any, AocError, Unit] =
    for
      input00 <- day00.readFile("inputs/Day00.txt")
        .tapError(e => ZIO.logError(s"Day00 input failed: $e"))
      _ <- ZIO.logDebug(s"Day00 input: $input00")
      _ <- Console.printLine(s"Day00 - part1: ${day00.part1(input00)}").orDie
      _ <- Console.printLine(s"Day00 - part2: ${day00.part2(input00)}").orDie

      input01 <- day01.readFile("inputs/Day01.txt")
        .tapError(e => ZIO.logError(s"Day01 input failed: $e"))
      _ <- ZIO.logDebug(s"Day01 input: $input01")
      _ <- Console.printLine(s"Day01 - part1: ${day01.part1(input01)}").orDie
      _ <- Console.printLine(s"Day01 - part2: ${day01.part2(input01)}").orDie

      input02 <- day02.readFile("inputs/Day02.txt")
        .tapError(e => ZIO.logError(s"Day02 input failed: $e"))
      _ <- ZIO.logDebug(s"Day02 input: $input02")
      _ <- Console.printLine(s"Day02 - part1: ${day02.part1(input02)}").orDie
      _ <- Console.printLine(s"Day02 - part2: ${day02.part2(input02)}").orDie

      input03 <- day03.readFile("inputs/Day03.txt")
        .tapError(e => ZIO.logError(s"Day03 input failed: $e"))
      _ <- ZIO.logDebug(s"Day03 input: $input03")
      _ <- Console.printLine(s"Day03 - part1: ${day03.part1(input03)}").orDie
      _ <- Console.printLine(s"Day03 - part2: ${day03.part2(input03)}").orDie

      input04 <- day04.readFile("inputs/Day04.txt")
        .tapError(e => ZIO.logError(s"Day04 input failed: $e"))
      _ <- ZIO.logDebug(s"Day04 input: $input04")
      part1Day04 <- ZIO.fromEither(day04.part1(input04))
        .tapError(e => ZIO.logError(s"Day04 part1 failed: $e"))
      _ <- Console.printLine(s"Day04 - part1: $part1Day04").orDie
      part2Day04 <- ZIO.fromEither(day04.part2(input04))
        .tapError(e => ZIO.logError(s"Day04 part2 failed: $e"))
      _ <- Console.printLine(s"Day04 - part2: $part2Day04").orDie

      input05 <- day05.readFile("inputs/Day05.txt")
        .tapError(e => ZIO.logError(s"Day05 input failed: $e"))
      _ <- ZIO.logDebug(s"Day05 input: $input05")
      _ <- Console.printLine(s"Day05 - part1: ${day05.part1(input05)}").orDie
      _ <- Console.printLine(s"Day05 - part2: ${day05.part2(input05)}").orDie

      input06 <- day06.readFile("inputs/Day06.txt")
        .tapError(e => ZIO.logError(s"Day06 input failed: $e"))
      _ <- ZIO.logDebug(s"Day06 input: $input06")
      _ <- Console.printLine(s"Day06 - part1: ${day06.part1(input06)}").orDie
      _ <- Console.printLine(s"Day06 - part2: ${day06.part2(input06)}").orDie

      input07 <- day07.readFile("inputs/Day07.txt")
        .tapError(e => ZIO.logError(s"Day07 input failed: $e"))
      _ <- ZIO.logDebug(s"Day07 input: $input07")
      _ <- Console.printLine(s"Day07 - part1: ${day07.part1(input07)}").orDie
      _ <- Console.printLine(s"Day07 - part2: ${day07.part2(input07)}").orDie

      input08 <- day08.readFile("inputs/Day08.txt")
        .tapError(e => ZIO.logError(s"Day08 input failed: $e"))
      _ <- ZIO.logDebug(s"Day08 input: $input08")
      _ <- Console.printLine(s"Day08 - part1: ${day08.part1(input08)}").orDie
      _ <- Console.printLine(s"Day08 - part2: ${day08.part2(input08)}").orDie

      input09 <- day09.readFile("inputs/Day09.txt")
        .tapError(e => ZIO.logError(s"Day09 input failed: $e"))
      _ <- ZIO.logDebug(s"Day09 input: $input09")
      _ <- Console.printLine(s"Day09 - part1: ${day09.part1(input09)}").orDie

      input10 <- day10.readFile("inputs/Day10.txt")
        .tapError(e => ZIO.logError(s"Day10 input failed: $e"))
      _ <- ZIO.logDebug(s"Day10 input: $input10")
      _ <- Console.printLine(s"Day10 - part1: ${day10.part1(input10)}").orDie
      _ <- Console.printLine(s"Day10 - part2: ${day10.part2(input10)}").orDie

      input11 <- day11.readFile("inputs/Day11.txt")
        .tapError(e => ZIO.logError(s"Day11 input failed: $e"))
      _ <- ZIO.logDebug(s"Day11 input: $input11")
      _ <- Console.printLine(s"Day11 - part1: ${day11.part1(input11)}").orDie
      _ <- Console.printLine(s"Day11 - part2: ${day11.part2(input11)}").orDie

      input12 <- day12.readFile("inputs/Day12.txt")
        .tapError(e => ZIO.logError(s"Day12 input failed: $e"))
      _ <- ZIO.logDebug(s"Day12 input: $input12")
      _ <- Console.printLine(s"Day12 - part1: ${day12.part1(input12)}").orDie
      _ <- Console.printLine(s"Day12 - part2: ${day12.part2(input12)}").orDie

      input13 <- day13.readFile("inputs/Day13.txt")
        .tapError(e => ZIO.logError(s"Day13 input failed: $e"))
      _ <- ZIO.logDebug(s"Day13 input: $input13")
      _ <- Console.printLine(s"Day13 - part1: ${day13.part1(input13)}").orDie
      _ <- Console.printLine(s"Day13 - part2: ${day13.part2(input13)}").orDie

      input14 <- day14.readFile("inputs/Day14.txt")
        .tapError(e => ZIO.logError(s"Day14 input failed: $e"))
      _ <- ZIO.logDebug(s"Day14 input: $input14")
      _ <- Console.printLine(s"Day14 - part1: ${day14.part1(input14)}").orDie
      _ <- Console.printLine(s"Day14 - part2: ${day14.part2(input14)}").orDie

      input15 <- day15.readFile("inputs/Day15.txt")
        .tapError(e => ZIO.logError(s"Day15 input failed: $e"))
      _ <- ZIO.logDebug(s"Day15 input: $input15")
      _ <- Console.printLine(s"Day15 - part1: ${day15.part1(input15)}").orDie
      _ <- Console.printLine(s"Day15 - part2: ${day15.part2(input15)}").orDie

      input16 <- day16.readFile("inputs/Day16.txt")
        .tapError(e => ZIO.logError(s"Day16 input failed: $e"))
      _ <- ZIO.logDebug(s"Day16 input: $input16")
      part1Day16 <- ZIO.fromEither(day16.part1(input16))
        .tapError(e => ZIO.logError(s"Day16 part1 failed: $e"))
      _ <- Console.printLine(s"Day16 - part1: $part1Day16").orDie
      part2Day16 <- ZIO.fromEither(day16.part2(input16))
        .tapError(e => ZIO.logError(s"Day16 part2 failed: $e"))
      _ <- Console.printLine(s"Day16 - part2: $part2Day16").orDie

      input17 <- day17.readFile("inputs/Day17.txt")
        .tapError(e => ZIO.logError(s"Day17 input failed: $e"))
      _ <- ZIO.logDebug(s"Day17 input: $input17")
      _ <- Console.printLine(s"Day17 - part1: ${day17.part1(input17)}").orDie
      _ <- Console.printLine(s"Day17 - part2: ${day17.part2(input17)}").orDie

      input18 <- day18.readFile("inputs/Day18.txt")
        .tapError(e => ZIO.logError(s"Day18 input failed: $e"))
      _ <- ZIO.logDebug(s"Day18 input: $input18")
      _ <- Console.printLine(s"Day18 - part1: ${day18.part1(input18)}").orDie
      _ <- Console.printLine(s"Day18 - part2: ${day18.part2(input18)}").orDie
    yield ()

  def run: ZIO[Any, Any, Unit] = program.tapError(e => ZIO.logError(s"run failed: $e"))
end Main

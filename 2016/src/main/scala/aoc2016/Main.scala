package aoc2016

import aoc2016.util.AocError
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
      answer02 = day01.part2(input01).fold("no repeated location")(_.toString)
      _ <- Console.printLine(s"Day01 - part2: $answer02").orDie
      input02 <- day02.readFile("inputs/Day02.txt")
        .tapError(e => ZIO.logError(s"Day02 input failed: $e"))
      _ <- ZIO.logDebug(s"Day02 input: $input02")
      _ <- Console.printLine(s"Day02 - part1: ${day02.part1(input02)}").orDie
      _ <- Console.printLine(s"Day02 - part2: ${day02.part2(input02)}").orDie
      input03 <- day03.readFile("inputs/Day03.txt")
        .tapError(e => ZIO.logError(s"Day03 input failed: $e"))
      _ <- ZIO.logDebug(s"Day03 input: $input03")
      _ <- Console.printLine(s"Day03 - part1: ${day03.part1(input03)}").orDie
      answer03Part2 <- ZIO.fromEither(day03.part2(input03))
      _ <- Console.printLine(s"Day03 - part2: $answer03Part2").orDie
      input04 <- day04.readFile("inputs/Day04.txt")
        .tapError(e => ZIO.logError(s"Day04 input failed: $e"))
      _ <- ZIO.logDebug(s"Day04 input: $input04")
      _ <- Console.printLine(s"Day04 - part1: ${day04.part1(input04)}").orDie
      answer04Part2 = day04.part2(input04).fold("North Pole object storage not found")(_.toString)
      _ <- Console.printLine(s"Day04 - part2: $answer04Part2").orDie
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
    yield ()

  def run: ZIO[Any, Any, Unit] = program.tapError(e => ZIO.logError(s"run failed: $e"))
end Main

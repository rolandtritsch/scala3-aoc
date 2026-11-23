package aoc2015

import zio.*

object Main extends ZIOAppDefault:

  private val program: ZIO[Any, AocError, Unit] =
    for
      input00 <- Day00.readFile("inputs/Day00.txt")
      _ <- Console.printLine(s"Day00 - part1: ${Day00.part1(input00)}").orDie
      _ <- Console.printLine(s"Day00 - part2: ${Day00.part2(input00)}").orDie

      input01 <- Day01.readFile("inputs/Day01.txt")
      _ <- Console.printLine(s"Day01 - part1: ${Day01.part1(input01)}").orDie
      _ <- Console.printLine(s"Day01 - part2: ${Day01.part2(input01)}").orDie

      input02 <- Day02.readFile("inputs/Day02.txt")
      _ <- Console.printLine(s"Day02 - part1: ${Day02.part1(input02)}").orDie
      _ <- Console.printLine(s"Day02 - part2: ${Day02.part2(input02)}").orDie

      input03 <- Day03.readFile("inputs/Day03.txt")
      _ <- Console.printLine(s"Day03 - part1: ${Day03.part1(input03)}").orDie
      _ <- Console.printLine(s"Day03 - part2: ${Day03.part2(input03)}").orDie

      input04 <- Day04.readFile("inputs/Day04.txt")
      part1Day04 <- ZIO.fromEither(Day04.part1(input04))
      _ <- Console.printLine(s"Day04 - part1: $part1Day04").orDie
      part2Day04 <- ZIO.fromEither(Day04.part2(input04))
      _ <- Console.printLine(s"Day04 - part2: $part2Day04").orDie

      input05 <- Day05.readFile("inputs/Day05.txt")
      _ <- Console.printLine(s"Day05 - part1: ${Day05.part1(input05)}").orDie
      _ <- Console.printLine(s"Day05 - part2: ${Day05.part2(input05)}").orDie

      input06 <- Day06.readFile("inputs/Day06.txt")
      part1Day06 <- ZIO.fromEither(Day06.part1(input06))
      _ <- Console.printLine(s"Day06 - part1: $part1Day06").orDie
      part2Day06 <- ZIO.fromEither(Day06.part2(input06))
      _ <- Console.printLine(s"Day06 - part2: $part2Day06").orDie
    yield ()

  def run: ZIO[Any, Any, Unit] = program
end Main

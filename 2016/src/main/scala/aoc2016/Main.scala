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
    yield ()

  def run: ZIO[Any, Any, Unit] = program.tapError(e => ZIO.logError(s"run failed: $e"))
end Main

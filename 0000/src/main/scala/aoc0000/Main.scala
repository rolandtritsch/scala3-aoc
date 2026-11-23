package aoc0000

import zio.*

object Main extends ZIOAppDefault:

  private val program: ZIO[Any, AocError, Unit] =
    for
      input00 <- Day00.readFile("inputs/Day00.txt")
      _ <- ZIO.logDebug(s"Day00 input: $input00")
      _ <- Console.printLine(s"Day00 - part1: ${Day00.part1(input00)}").orDie
      _ <- Console.printLine(s"Day00 - part2: ${Day00.part2(input00)}").orDie
    yield ()

  def run: ZIO[Any, Any, Unit] = program.tapError(e => ZIO.logError(s"run failed: $e"))
end Main

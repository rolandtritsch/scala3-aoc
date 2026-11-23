package aoc2015

import aoc2015.Day04.*
import zio.*
import zio.test.*

object Day04Test extends ZIOSpecDefault:

  def spec: Spec[Any, Any] = suite("Day04")(
    test("md5Hex - independent RFC 1321 vectors"):
      assertTrue(
        md5Hex("") == "d41d8cd98f00b204e9800998ecf8427e",
        md5Hex("abc") == "900150983cd24fb0d6963f7d28e17f72",
      )
    ,
    test("part1 - official samples"):
      assertTrue(part1("abcdef") == Right(609043), part1("pqrstuv") == Right(1048970))
    ,
    test("part1 - empty key is rejected"):
      assertTrue(part1("").isLeft)
    ,
    test("part1 failure logs an error via tapError"):
      for
        exit <- ZIO.fromEither(part1("")).tapError(e => ZIO.logError(s"part1 failed: $e")).exit
        logs <- ZTestLogger.logOutput
      yield assertTrue(
        exit.isFailure,
        logs.exists(entry =>
          entry.logLevel == LogLevel.Error && entry.message().contains("part1 failed")
        ),
      )
    ,
    test("part1 - found hash actually has the required prefix"):
      assertTrue(part1("bgvyzdsv") == Right(254575))
    ,
    test("part1 - real input"):
      readFile("inputs/Day04.txt").map(input => assertTrue(part1(input) == Right(254575)))
    ,
    test("part2 - empty key is rejected"):
      assertTrue(part2("").isLeft)
    ,
    test("part2 - found hash actually has the required prefix"):
      part2("bgvyzdsv") match
        case Right(n) => assertTrue(
            md5Hex("bgvyzdsv" + n).startsWith("000000"),
            !md5Hex("bgvyzdsv" + (n - 1)).startsWith("000000"),
          )
        case Left(error) => assertNever(s"expected a result, got: $error")
    ,
    test("part2 - real input"):
      readFile("inputs/Day04.txt").map(input => assertTrue(part2(input) == Right(1038736))),
  )

end Day04Test

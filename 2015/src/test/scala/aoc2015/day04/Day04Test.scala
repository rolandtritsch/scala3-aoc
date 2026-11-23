package aoc2015.day04

import aoc2015.util.AocError
import zio.*
import zio.test.*

object Day04Test extends ZIOSpecDefault:

  def spec: Spec[Any, Any] = suite("Day04")(
    test("parse - surrounding whitespace is removed"):
      assertTrue(parse("sample", " abcdef\n") == parse("sample", "abcdef"))
    ,
    test("readFile - empty key fails at the boundary"):
      readFile("inputs/Day04Invalid.txt").either.map(result =>
        assertTrue(result.left.exists {
          case AocError.ParseError("inputs/Day04Invalid.txt", _) => true
          case _                                                 => false
        })
      )
    ,
    test("md5Hex - independent RFC 1321 vectors"):
      assertTrue(
        md5Hex("") == "d41d8cd98f00b204e9800998ecf8427e",
        md5Hex("abc") == "900150983cd24fb0d6963f7d28e17f72",
      )
    ,
    test("part1 - official samples"):
      assertTrue(
        parse("sample", "abcdef").flatMap(part1) == Right(609043),
        parse("sample", "pqrstuv").flatMap(part1) == Right(1048970),
      )
    ,
    test("parse - empty key is rejected"):
      assertTrue(parse("sample", "").isLeft)
    ,
    test("input failure logs an error via tapError"):
      for
        exit <- ZIO.fromEither(parse("sample", "")).tapError(e => ZIO.logError(s"input failed: $e"))
          .exit
        logs <- ZTestLogger.logOutput
      yield assertTrue(
        exit.isFailure,
        logs.exists(entry =>
          entry.logLevel == LogLevel.Error && entry.message().contains("input failed")
        ),
      )
    ,
    test("part1 - found hash actually has the required prefix"):
      assertTrue(parse("sample", "bgvyzdsv").flatMap(part1) == Right(254575))
    ,
    test("part1 - real input"):
      readFile("inputs/Day04.txt").map(input => assertTrue(part1(input) == Right(254575)))
    ,
    test("parse - whitespace-only or multiple keys are rejected"):
      assertTrue(parse("sample", " \n\t").isLeft, parse("sample", "abc def").isLeft)
    ,
    test("part2 - found hash actually has the required prefix"):
      parse("sample", "bgvyzdsv").flatMap(part2).fold(
        error => assertNever(s"expected a result, got: $error"),
        n =>
          assertTrue(
            md5Hex("bgvyzdsv" + n).startsWith("000000"),
            !md5Hex("bgvyzdsv" + (n - 1)).startsWith("000000"),
          ),
      )
    ,
    test("part2 - real input"):
      readFile("inputs/Day04.txt").map(input => assertTrue(part2(input) == Right(1038736))),
  )

end Day04Test

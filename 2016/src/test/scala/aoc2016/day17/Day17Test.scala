package aoc2016.day17

import zio.test.*

object Day17Test extends ZIOSpecDefault:

  def spec: Spec[Any, Any] = suite("Day17")(
    test("parse - accepts a single non-empty passcode without whitespace"):
      assertTrue(
        parse("sample", "abc").map(_.value) == Right("abc"),
        parse("sample", "abc\n").map(_.value) == Right("abc"),
        parse("sample", "abc\r\n").map(_.value) == Right("abc"),
      )
    ,
    test("parse - rejects empty, blank, whitespace-containing, and multiple-line input"):
      assertTrue(
        Seq("", " \n", "ab c\n", "abc\nxyz", "abc\n\n")
          .forall(input => parse("sample", input).isLeft)
      )
    ,
    test("part1 - official examples"):
      assertTrue(
        parse("sample", "ihgpwlah").map(part1) == Right(Some("DDRRRD")),
        parse("sample", "kglvqrro").map(part1) == Right(Some("DDUDRLRRUDRD")),
        parse("sample", "ulqzkmiv").map(part1) == Right(Some("DRURDRUDDLLDLUURRDULRLDUUDDDRR")),
      )
    ,
    test("part2 - official examples"):
      assertTrue(
        parse("sample", "ihgpwlah").map(part2) == Right(Some(370)),
        parse("sample", "kglvqrro").map(part2) == Right(Some(492)),
        parse("sample", "ulqzkmiv").map(part2) == Right(Some(830)),
      )
    ,
    test("readFile and part1 - personal input"):
      readFile("inputs/Day17.txt")
        .map(passcode => assertTrue(part1(passcode) == Some("RDRLDRDURD")))
    ,
    test("part2 - personal input"):
      readFile("inputs/Day17.txt").map(passcode => assertTrue(part2(passcode) == Some(596))),
  )

  end spec

end Day17Test

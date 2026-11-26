package aoc2016.day04

import aoc2016.util.AocError
import zio.test.*

object Day04Test extends ZIOSpecDefault:

  private val exampleRooms = Seq(
    Room(Seq("aaaaa", "bbb", "z", "y", "x"), 123, "abxyz"),
    Room(Seq("a", "b", "c", "d", "e", "f", "g", "h"), 987, "abcde"),
    Room(Seq("not", "a", "real", "room"), 404, "oarel"),
    Room(Seq("totally", "real", "room"), 200, "decoy"),
  )

  def spec: Spec[Any, Any] = suite("Day04")(
    test("parse - converts official examples and permits CRLF line endings"):
      val input = "aaaaa-bbb-z-y-x-123[abxyz]\r\n" + "a-b-c-d-e-f-g-h-987[abcde]\r\n" +
        "not-a-real-room-404[oarel]\r\n" + "totally-real-room-200[decoy]\r\n"
      assertTrue(parse("sample", input) == Right(exampleRooms))
    ,
    test("parse - rejects empty, blank, malformed, and overflowing input"):
      assertTrue(
        Seq(
          "",
          " \n",
          "aaaaa-bbb-z-y-x-123[abxyz]\n\nnot-a-real-room-404[oarel]",
          "a--b-123[abxyz]",
          "a-b-123[abcd]",
          "a-b-123[abcdef]",
          "a-b-xyz[abcde]",
          "a-b--1[abcde]",
          "A-b-123[abcde]",
          "a-b-123[abcde]trailing",
          "a-b-2147483648[abcde]",
        ).forall(input => parse("sample", input).isLeft)
      )
    ,
    test("parse - reports the malformed line"):
      val input = "not-a-real-room-404[oarel]\ntotally-real-room-200[decoy"
      assertTrue(parse("sample", input).left.exists {
        case AocError.ParseError("sample", message) => message.contains("line 2")
        case _                                      => false
      })
    ,
    test("part1 - official examples sum to 1514"):
      assertTrue(part1(exampleRooms) == 1514)
    ,
    test("readFile - personal input"):
      readFile("inputs/Day04.txt").map(input => assertTrue(input.nonEmpty))
    ,
    test("part1 - personal input"):
      readFile("inputs/Day04.txt").map(input => assertTrue(part1(input) == 137896)),
  )

  end spec

end Day04Test

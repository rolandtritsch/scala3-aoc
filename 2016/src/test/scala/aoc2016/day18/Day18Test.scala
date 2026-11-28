package aoc2016.day18

import aoc2016.util.AocError
import zio.test.*

object Day18Test extends ZIOSpecDefault:

  def spec: Spec[Any, Any] = suite("Day18")(
    test("parse - reads a single row of tiles and ignores blank lines"):
      assertTrue(
        parse("sample", "..^.\n") == Right(Input(Seq(Tile.Safe, Tile.Safe, Tile.Trap, Tile.Safe))),
        parse("sample", "\n ^.^ \n\n") == Right(Input(Seq(Tile.Trap, Tile.Safe, Tile.Trap))),
      )
    ,
    test("parse - rejects empty, multi-row, and non-tile input"):
      assertTrue(
        Seq("", "\n", "..\n^^", ".x.", "^^ ^^").forall(input => parse("sample", input).isLeft)
      )
    ,
    test("parse - reports the input filename"):
      assertTrue(parse("sample", ".x.").left.exists {
        case AocError.ParseError("sample", _) => true
        case _                                => false
      })
    ,
    test("part1 - small official example"):
      assertTrue(
        parse("sample", "..^^.").map(input => countSafeTiles(rows(input.firstRow, 3))) == Right(6)
      )
    ,
    test("part1 - larger official example"):
      assertTrue(
        parse("sample", ".^^.^.^^^^").map(input => countSafeTiles(rows(input.firstRow, 10))) ==
          Right(38)
      )
    ,
    test("readFile - personal input"):
      readFile("inputs/Day18.txt").map(input => assertTrue(input.firstRow.size == 100))
    ,
    test("part1 - personal input"):
      readFile("inputs/Day18.txt").map(input => assertTrue(part1(input) == 1978)),
  )

  end spec

end Day18Test

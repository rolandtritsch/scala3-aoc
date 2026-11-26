package aoc2015.day25

import aoc2015.util.AocError
import zio.test.*

object Day25Test extends ZIOSpecDefault:

  private val officialCodes = Seq(
    Seq(20151125L, 18749137L, 17289845L, 30943339L, 10071777L, 33511524L),
    Seq(31916031L, 21629792L, 16929656L, 7726640L, 15514188L, 4041754L),
    Seq(16080970L, 8057251L, 1601130L, 7981243L, 11661866L, 16474243L),
    Seq(24592653L, 32451966L, 21345942L, 9380097L, 10600672L, 31527494L),
    Seq(77061L, 17552253L, 28094349L, 6899651L, 9250759L, 31663883L),
    Seq(33071741L, 6796745L, 25397450L, 24659492L, 1534922L, 27995004L),
  )

  private def message(row: Int, column: Int): String =
    s"To continue, please consult the code grid in the manual.  Enter the code at row $row, column $column."

  private def parsedCoordinate(row: Int, column: Int): Either[AocError, Coordinate] =
    parse("sample", message(row, column))

  def spec: Spec[Any, Any] = suite("Day25")(
    test("parse - official message and surrounding whitespace"):
      val input = s"\n  ${message(2981, 3075)}\n"
      assertTrue(parse("sample", input).map(c => (c.row, c.column)) == Right((2981, 3075)))
    ,
    test("parse - malformed, non-positive, and overflowing coordinates"):
      val invalid = Seq(
        "",
        "row 1, column 1",
        message(1, 1) + "\nextra",
        message(0, 1),
        message(1, 0),
        "To continue, please consult the code grid in the manual.  Enter the code at row -1, column 1.",
        message(1, 1).replace("column 1", "column one"),
        message(1, 1).replace("row 1", "row 2147483648"),
      )
      assertTrue(invalid.forall(input => parse("sample", input).isLeft))
    ,
    test("codeIndex - official diagonal positions"):
      assertTrue(
        parsedCoordinate(1, 1).map(codeIndex) == Right(BigInt(1)),
        parsedCoordinate(2, 1).map(codeIndex) == Right(BigInt(2)),
        parsedCoordinate(1, 2).map(codeIndex) == Right(BigInt(3)),
        parsedCoordinate(4, 2).map(codeIndex) == Right(BigInt(12)),
        parsedCoordinate(1, 5).map(codeIndex) == Right(BigInt(15)),
        parsedCoordinate(6, 1).map(codeIndex) == Right(BigInt(16)),
      )
    ,
    test("part1 - complete official six-by-six example"):
      val results =
        for
          (codes, row) <- officialCodes.zipWithIndex
          (expected, column) <- codes.zipWithIndex
        yield parsedCoordinate(row + 1, column + 1).map(part1) == Right(expected)
      assertTrue(results.forall(result => result))
    ,
    test("part1 - real input"):
      readFile("inputs/Day25.txt").map(coordinate => assertTrue(part1(coordinate) == 9132360L))
    ,
    test("readFile - typed puzzle coordinate"):
      readFile("inputs/Day25.txt")
        .map(coordinate => assertTrue(coordinate.row == 2981, coordinate.column == 3075))
    ,
    test("readFile - missing resource"):
      readFile("inputs/Day25-missing.txt").either.map(result =>
        assertTrue(result.left.exists {
          case AocError.FileReadError("inputs/Day25-missing.txt", _) => true
          case _                                                     => false
        })
      ),
  )
  end spec

end Day25Test

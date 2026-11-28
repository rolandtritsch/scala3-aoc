package aoc2016.day22

import aoc2016.util.AocError
import zio.test.*

object Day22Test extends ZIOSpecDefault:

  private val example: String = """root@ebhq-gridcenter# df -h
                                  |Filesystem              Size  Used  Avail  Use%
                                  |/dev/grid/node-x0-y0     10T    8T     2T   80%
                                  |/dev/grid/node-x0-y1     11T    6T     5T   54%
                                  |/dev/grid/node-x0-y2     32T   28T     4T   87%
                                  |/dev/grid/node-x1-y0      9T    7T     2T   77%
                                  |/dev/grid/node-x1-y1      8T    0T     8T    0%
                                  |/dev/grid/node-x1-y2     11T    7T     4T   63%
                                  |/dev/grid/node-x2-y0     10T    6T     4T   60%
                                  |/dev/grid/node-x2-y1      9T    8T     1T   88%
                                  |/dev/grid/node-x2-y2      9T    6T     3T   66%
                                  |""".stripMargin

  def spec: Spec[Any, Any] = suite("Day22")(
    test("parse - reads nodes and skips the two header lines"):
      assertTrue(
        parse("sample", example) == Right(Input(Seq(
          Node(0, 0, 10L, 8L, 2L),
          Node(0, 1, 11L, 6L, 5L),
          Node(0, 2, 32L, 28L, 4L),
          Node(1, 0, 9L, 7L, 2L),
          Node(1, 1, 8L, 0L, 8L),
          Node(1, 2, 11L, 7L, 4L),
          Node(2, 0, 10L, 6L, 4L),
          Node(2, 1, 9L, 8L, 1L),
          Node(2, 2, 9L, 6L, 3L),
        )))
      )
    ,
    test("parse - rejects empty and malformed input"):
      assertTrue(
        Seq("", "\n", "Filesystem  Size  Used  Avail  Use%", "/dev/grid/node-x0-y0 10T 8T 2T")
          .forall(input => parse("sample", input).isLeft)
      )
    ,
    test("parse - reports the input filename"):
      assertTrue(parse("sample", "nonsense").left.exists {
        case AocError.ParseError("sample", _) => true
        case _                                => false
      })
    ,
    test("part1 - official example has 7 viable pairs"):
      val input = parse("sample", example).toOption.get
      assertTrue(part1(input) == 7)
    ,
    test("part1 - personal input"):
      readFile("inputs/Day22.txt").map(input => assertTrue(part1(input) == 924)),
  )

  end spec

end Day22Test

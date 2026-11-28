package aoc2016.day18

import scala.annotation.tailrec

/** The total number of rows in the room for part two, including the starting row. */
private val Part2RowCount = 400000

/** The part-two answer: the number of safe tiles across all `Part2RowCount` rows. */
def part2(input: Input): Int = countSafe(input.firstRow, Part2RowCount)

/** Count safe tiles over `rowsLeft` rows, keeping only the current row in memory.
  *
  * The room is far too tall to materialise every row, so this streams one row at a time and
  * accumulates the running total; memory stays proportional to the row width, not the row count.
  */
@tailrec
private def countSafe(row: Seq[Tile], rowsLeft: Int, total: Int = 0): Int =
  if rowsLeft == 0 then total else countSafe(next(row), rowsLeft - 1, total + row.count(_.isSafe))

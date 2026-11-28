package aoc2016.day18

/** The total number of rows in the room, including the starting row. */
private val RowCount = 40

/** The part-one answer: the number of safe tiles across all `RowCount` rows. */
def part1(input: Input): Int = countSafeTiles(rows(input.firstRow, RowCount))

/** Build `count` rows beginning with `first`, each row derived from the one above it. */
def rows(first: Seq[Tile], count: Int): Seq[Seq[Tile]] = (1 until count)
  .scanLeft(first)((previous, _) => next(previous))

/** Derive the next row: a tile is a trap exactly when its left and right neighbours differ. */
def next(previous: Seq[Tile]): Seq[Tile] = previous.indices.map { index =>
  if tileAt(previous, index - 1) != tileAt(previous, index + 1) then Tile.Trap else Tile.Safe
}.toSeq

/** The tile at `index`, treating positions off either end of the row as safe. */
def tileAt(row: Seq[Tile], index: Int): Tile =
  if index < 0 || index >= row.length then Tile.Safe else row(index)

/** Count the safe tiles across every row. */
def countSafeTiles(allRows: Seq[Seq[Tile]]): Int = allRows.iterator.map(_.count(_.isSafe)).sum

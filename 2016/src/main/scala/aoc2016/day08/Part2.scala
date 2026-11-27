package aoc2016.day08

import aoc2016.util.AocError

private val LetterWidth = 5

private val Glyphs = Map(
  Seq(".##..", "#..#.", "#....", "#....", "#..#.", ".##..") -> 'C',
  Seq("####.", "#....", "###..", "#....", "#....", "####.") -> 'E',
  Seq("####.", "#....", "###..", "#....", "#....", "#....") -> 'F',
  Seq("#....", "#....", "#....", "#....", "#....", "####.") -> 'L',
  Seq(".##..", "#..#.", "#..#.", "#..#.", "#..#.", ".##..") -> 'O',
  Seq(".###.", "#....", "#....", ".##..", "...#.", "###..") -> 'S',
  Seq("#...#", "#...#", ".#.#.", "..#..", "..#..", "..#..") -> 'Y',
)

/** Decode the capital letters displayed after applying every screen instruction. */
def part2(instructions: Seq[Instruction]): Either[AocError, String] =
  val screen = runScreen(ScreenWidth, ScreenHeight, instructions)
  (0 until ScreenWidth by LetterWidth).foldLeft[Either[AocError, String]](Right("")) {
    case (Right(code), leftEdge) => decodeGlyph(glyphAt(screen, leftEdge))
        .map(letter => code :+ letter)
    case (error @ Left(_), _) => error
  }
end part2

private def glyphAt(screen: Screen, leftEdge: Int): Seq[String] = (0 until ScreenHeight).map { row =>
  (leftEdge until leftEdge + LetterWidth)
    .map(column => if screen.lit.contains(Pixel(column, row)) then '#' else '.').mkString
}

private def decodeGlyph(glyph: Seq[String]): Either[AocError, Char] = Glyphs.get(glyph)
  .toRight(AocError.ValidationError("Day08 display contains an unrecognized 5x6 glyph"))

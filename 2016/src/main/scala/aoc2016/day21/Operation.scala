package aoc2016.day21

/** One scrambling operation, parsed from a line of the puzzle input. */
enum Operation:
  /** Exchange the letters at two positions. */
  case SwapPosition(x: Int, y: Int)

  /** Exchange the two letters wherever they appear. */
  case SwapLetter(x: Char, y: Char)

  /** Rotate the whole string left or right by a number of steps. */
  case Rotate(steps: Int, direction: Rotation)

  /** Rotate right one plus the index of a letter, plus one more when that index is at least 4. */
  case RotateLetter(letter: Char)

  /** Reverse the span of letters from one position through another, inclusive. */
  case Reverse(x: Int, y: Int)

  /** Remove the letter at one position and insert it at another. */
  case Move(x: Int, y: Int)

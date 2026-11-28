package aoc2016.day21

/** The part-one answer: the password `abcdefgh` after every scrambling operation is applied.
  *
  * Each operation is a pure string-to-string transformation, folded over the operations in input
  * order. Indices are zero-based and positions are inclusive where the puzzle says so.
  */
def part1(input: Input): String = scramble("abcdefgh", input.operations)

/** Apply every operation in order to `password`, returning the scrambled result. */
def scramble(password: String, operations: Seq[Operation]): String = operations
  .foldLeft(password)(apply)

/** Apply one operation to a password, dispatching exhaustively over the domain. */
private def apply(password: String, operation: Operation): String = operation match
  case Operation.SwapPosition(x, y)   => swapPositions(password, x, y)
  case Operation.SwapLetter(a, b)     => swapLetters(password, a, b)
  case Operation.Rotate(steps, dir)   => rotate(password, steps, dir)
  case Operation.RotateLetter(letter) => rotateByLetter(password, letter)
  case Operation.Reverse(x, y)        => reverse(password, x, y)
  case Operation.Move(x, y)           => move(password, x, y)
end apply

/** Rotate right one, plus the letter's index, plus one more when that index is at least four. */
private def rotateByLetter(password: String, letter: Char): String =
  val index = password.indexOf(letter)
  val extra = if index >= 4 then 1 else 0
  rotate(password, 1 + index + extra, Rotation.Right)

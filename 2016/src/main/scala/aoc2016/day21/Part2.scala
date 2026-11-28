package aoc2016.day21

/** The part-two answer: the password that scrambles to `fbgdceah` under every operation.
  *
  * Un-scrambling applies the inverse of each operation in reverse order. Indices are zero-based and
  * positions are inclusive where the puzzle says so.
  */
def part2(input: Input): String = unscramble("fbgdceah", input.operations)

/** Undo every operation in reverse order, returning the original password. */
def unscramble(password: String, operations: Seq[Operation]): String = operations.reverseIterator
  .foldLeft(password)(unapply)

/** Apply the inverse of one operation to a password, dispatching exhaustively over the domain. */
private def unapply(password: String, operation: Operation): String = operation match
  case Operation.SwapPosition(x, y)   => swapPositions(password, x, y)
  case Operation.SwapLetter(a, b)     => swapLetters(password, a, b)
  case Operation.Rotate(steps, dir)   => rotate(password, steps, invert(dir))
  case Operation.RotateLetter(letter) => unrotateByLetter(password, letter)
  case Operation.Reverse(x, y)        => reverse(password, x, y)
  case Operation.Move(x, y)           => move(password, y, x)
end unapply

/** The opposite rotation direction. */
private def invert(direction: Rotation): Rotation = direction match
  case Rotation.Left  => Rotation.Right
  case Rotation.Right => Rotation.Left

/** Undo a rotate-by-letter: recover the pre-rotation index of `letter`, then rotate left by its
  * shift. For the length-eight puzzle domain the forward index map is a bijection, so exactly one
  * pre-rotation index lands on the letter's current position.
  */
private def unrotateByLetter(password: String, letter: Char): String =
  val n = password.length
  val position = password.indexOf(letter)
  val index = (0 until n).find(i => (2 * i + 1 + (if i >= 4 then 1 else 0)) % n == position).get
  rotate(password, 1 + index + (if index >= 4 then 1 else 0), Rotation.Left)

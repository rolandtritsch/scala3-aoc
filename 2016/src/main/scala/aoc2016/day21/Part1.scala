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

/** Exchange the characters at two positions. */
private def swapPositions(password: String, x: Int, y: Int): String =
  val chars = password.toCharArray
  val tmp = chars(x)
  chars(x) = chars(y)
  chars(y) = tmp
  new String(chars)
end swapPositions

/** Exchange the two letters wherever they occur. */
private def swapLetters(password: String, x: Char, y: Char): String = password
  .map(c => if c == x then y else if c == y then x else c)

/** Rotate the whole string left or right by `steps` positions. */
private def rotate(password: String, steps: Int, direction: Rotation): String =
  val n = password.length
  val shift = math.floorMod(steps, n)
  direction match
    case Rotation.Left  => password.drop(shift) + password.take(shift)
    case Rotation.Right => password.takeRight(shift) + password.dropRight(shift)
end rotate

/** Rotate right one, plus the letter's index, plus one more when that index is at least four. */
private def rotateByLetter(password: String, letter: Char): String =
  val index = password.indexOf(letter)
  val extra = if index >= 4 then 1 else 0
  rotate(password, 1 + index + extra, Rotation.Right)

/** Reverse the inclusive span of positions from `x` through `y`. */
private def reverse(password: String, x: Int, y: Int): String = password.take(x) +
  password.slice(x, y + 1).reverse + password.drop(y + 1)

/** Remove the letter at `x` and insert it so that it ends up at `y`. */
private def move(password: String, x: Int, y: Int): String =
  val letter = password.charAt(x)
  val without = password.take(x) + password.drop(x + 1)
  without.take(y) + letter + without.drop(y)

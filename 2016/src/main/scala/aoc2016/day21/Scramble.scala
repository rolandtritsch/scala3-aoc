package aoc2016.day21

/** Primitive string transforms shared by scrambling (part 1) and un-scrambling (part 2).
  *
  * Each function is a pure string-to-string operation matching one family of puzzle instructions.
  * Positions are zero-based and spans are inclusive where the puzzle says so.
  */

/** Exchange the characters at two positions. */
def swapPositions(password: String, x: Int, y: Int): String =
  val chars = password.toCharArray
  val tmp = chars(x)
  chars(x) = chars(y)
  chars(y) = tmp
  new String(chars)
end swapPositions

/** Exchange the two letters wherever they occur. */
def swapLetters(password: String, x: Char, y: Char): String = password
  .map(c => if c == x then y else if c == y then x else c)

/** Rotate the whole string left or right by `steps` positions. */
def rotate(password: String, steps: Int, direction: Rotation): String =
  val n = password.length
  val shift = math.floorMod(steps, n)
  direction match
    case Rotation.Left  => password.drop(shift) + password.take(shift)
    case Rotation.Right => password.takeRight(shift) + password.dropRight(shift)
end rotate

/** Reverse the inclusive span of positions from `x` through `y`. */
def reverse(password: String, x: Int, y: Int): String = password.take(x) + password.slice(x, y + 1)
  .reverse + password.drop(y + 1)

/** Remove the letter at `x` and insert it so that it ends up at `y`. */
def move(password: String, x: Int, y: Int): String =
  val letter = password.charAt(x)
  val without = password.take(x) + password.drop(x + 1)
  without.take(y) + letter + without.drop(y)

package aoc2016.day16

/** A single binary digit of the dragon-curve state. */
enum Bit:
  case Zero
  case One

  /** The opposite digit, used when building the reversed half of the curve. */
  def flip: Bit = this match
    case Bit.Zero => Bit.One
    case Bit.One  => Bit.Zero

end Bit

/** Parse one binary digit, or `None` for any other character. */
def bitFromChar(c: Char): Option[Bit] = PartialFunction.condOpt(c):
  case '0' => Bit.Zero
  case '1' => Bit.One

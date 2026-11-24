package aoc2015.day07

/** An operand to a gate: a literal 16-bit value or a reference to another wire. */
enum Operand:
  case Lit(value: Int)
  case Ref(name: String)
end Operand

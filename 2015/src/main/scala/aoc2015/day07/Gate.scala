package aoc2015.day07

/** A source feeding a single wire: a literal, another wire, or a bitwise gate.
  *
  * Wires hold 16-bit signals (0..65535), so every computed value is masked with 0xffff. `NOT` takes
  * a single wire operand; the binary gates accept a literal or wire on either side.
  */
enum Gate:
  case Literal(value: Int)
  case Wire(name: String)
  case And(left: Operand, right: Operand)
  case Or(left: Operand, right: Operand)
  case LShift(left: Operand, amount: Int)
  case RShift(left: Operand, amount: Int)
  case Not(left: String)
end Gate

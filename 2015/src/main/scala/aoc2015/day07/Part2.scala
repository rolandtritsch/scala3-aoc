package aoc2015.day07

/** Re-evaluate the circuit after rewiring wire `b`.
  *
  * Part two runs the circuit once to obtain the signal on wire `a`, feeds that value directly into
  * wire `b`, resets every other wire (including `a`), and evaluates the circuit again.
  */
def part2(instructions: Seq[Instruction]): Int =
  val originalA = signalOf("a", instructions)
  val overridden = instructions.map { instruction =>
    if instruction.output == "b" then Instruction.literal(originalA, "b") else instruction
  }
  signalOf("a", overridden)
end part2

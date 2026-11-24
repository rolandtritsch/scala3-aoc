package aoc2015.day07

/** Evaluate a wire's signal from a circuit of instructions, memoizing sub-results.
  *
  * Signals are 16-bit, so every gate masks its result with `0xffff`.
  */
private[day07] val signalMask: Int = 0xffff

private def operandValue(
    operand: Operand,
    env: Map[String, Gate],
    cache: scala.collection.mutable.Map[String, Int],
): Int = operand match
  case Operand.Lit(value) => value & signalMask
  case Operand.Ref(name)  => signal(name, env, cache)
end operandValue

private def resolve(
    gate: Gate,
    env: Map[String, Gate],
    cache: scala.collection.mutable.Map[String, Int],
): Int = gate match
  case Gate.Literal(value) => value & signalMask
  case Gate.Wire(name)     => signal(name, env, cache)
  case Gate.And(l, r) => (operandValue(l, env, cache) & operandValue(r, env, cache)) & signalMask
  case Gate.Or(l, r)  => (operandValue(l, env, cache) | operandValue(r, env, cache)) & signalMask
  case Gate.LShift(l, amount) => (operandValue(l, env, cache) << amount) & signalMask
  case Gate.RShift(l, amount) => (operandValue(l, env, cache) >> amount) & signalMask
  case Gate.Not(l)            => (~signal(l, env, cache)) & signalMask
end resolve

private def signal(
    name: String,
    env: Map[String, Gate],
    cache: scala.collection.mutable.Map[String, Int],
): Int = cache.getOrElseUpdate(name, resolve(env(name), env, cache))

/** The signal eventually provided to a single wire.
  *
  * @param name
  *   the wire whose signal is requested.
  * @param instructions
  *   the parsed circuit; duplicate output wires are rejected at the ReadFile boundary.
  */
def signalOf(name: String, instructions: Seq[Instruction]): Int =
  val env = instructions.map(i => i.output -> i.gate).toMap
  signal(name, env, scala.collection.mutable.Map.empty)

/** The signal provided to wire `a`, per the puzzle's question. */
def part1(instructions: Seq[Instruction]): Int = signalOf("a", instructions)

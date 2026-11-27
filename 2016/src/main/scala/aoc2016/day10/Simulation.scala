package aoc2016.day10

import scala.annotation.tailrec

private[day10] def simulate(input: Factory, targets: Option[(Int, Int)]): FactoryResult =
  // The work stack is a List because recipients may be processed in either order.
  @tailrec
  def loop(state: FactoryState, pending: List[Int]): FactoryResult = pending match
    case Nil         => FactoryResult(state.outputs, None)
    case bot :: rest => state.chips.getOrElse(bot, Seq.empty) match
        case Seq(first, second) =>
          val low = math.min(first, second)
          val high = math.max(first, second)
          val isTarget = targets.exists { case (firstTarget, secondTarget) =>
            (low == firstTarget && high == secondTarget) ||
            (low == secondTarget && high == firstTarget)
          }
          if isTarget then FactoryResult(state.outputs, Some(bot))
          else
            input.rules.get(bot) match
              case None       => loop(state, rest)
              case Some(rule) =>
                val cleared = state.copy(chips = state.chips.updated(bot, Seq.empty))
                val (afterLow, lowPending) = deliver(cleared, rule.low, low)
                val (afterHigh, highPending) = deliver(afterLow, rule.high, high)
                loop(afterHigh, lowPending ::: highPending ::: rest)
        case _ => loop(state, rest)
  end loop

  val initialState = FactoryState(input.initialChips, Map.empty)
  loop(initialState, input.initialChips.keys.toList)
end simulate

private def deliver(
    state: FactoryState,
    destination: Destination,
    value: Int,
): (FactoryState, List[Int]) = destination match
  case Destination.Bot(bot) =>
    val chips = state.chips.updated(bot, value +: state.chips.getOrElse(bot, Seq.empty))
    (state.copy(chips = chips), List(bot))
  case Destination.Output(number) =>
    val outputs = state.outputs.updated(number, state.outputs.getOrElse(number, Seq.empty) :+ value)
    (state.copy(outputs = outputs), Nil)
end deliver

package aoc2016.day10

import scala.annotation.tailrec

def part1(input: Factory): Option[Int] = findBotComparing(input, 17, 61)

private[day10] def findBotComparing(
    input: Factory,
    firstTarget: Int,
    secondTarget: Int,
): Option[Int] =
  // The work stack is a List because recipients may be processed in either order.
  @tailrec
  def loop(chips: Map[Int, Seq[Int]], pending: List[Int]): Option[Int] = pending match
    case Nil         => None
    case bot :: rest => chips.getOrElse(bot, Seq.empty) match
        case Seq(first, second) =>
          val low = math.min(first, second)
          val high = math.max(first, second)
          val matches = (low == firstTarget && high == secondTarget) ||
            (low == secondTarget && high == firstTarget)
          if matches then Some(bot)
          else
            input.rules.get(bot) match
              case None       => loop(chips, rest)
              case Some(rule) =>
                val cleared = chips.updated(bot, Seq.empty)
                val (afterLow, lowPending) = deliver(cleared, rule.low, low)
                val (afterHigh, highPending) = deliver(afterLow, rule.high, high)
                loop(afterHigh, lowPending ::: highPending ::: rest)
        case _ => loop(chips, rest)
  end loop

  loop(input.initialChips, input.initialChips.keys.toList)
end findBotComparing

private def deliver(
    chips: Map[Int, Seq[Int]],
    destination: Destination,
    value: Int,
): (Map[Int, Seq[Int]], List[Int]) = destination match
  case Destination.Bot(bot) =>
    (chips.updated(bot, value +: chips.getOrElse(bot, Seq.empty)), List(bot))
  case Destination.Output(_) => (chips, Nil)
end deliver

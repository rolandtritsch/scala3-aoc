package aoc2015.day22

/** An active effect with its remaining timer. */
final case class Effect(effectType: EffectType, timer: Int):

  /** Applies this effect at the start of a turn, returns (damage, armor, manaGain). */
  def applyEffect: (Int, Int, Int) = effectType match
    case EffectType.Shield   => (0, 7, 0)
    case EffectType.Poison   => (3, 0, 0)
    case EffectType.Recharge => (0, 0, 101)

  /** Decrements the timer, returns None if effect expires. */
  def tick: Option[Effect] = Option.when(timer > 1)(copy(timer = timer - 1))

end Effect

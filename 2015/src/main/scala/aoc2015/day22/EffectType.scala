package aoc2015.day22

/** Active effect types with their durations. */
enum EffectType:
  case Shield extends EffectType // +7 armor, lasts 6 turns
  case Poison extends EffectType // 3 damage/turn, lasts 6 turns
  case Recharge extends EffectType // +101 mana/turn, lasts 5 turns

  /** Initial timer (duration) for this effect. */
  def initialTimer: Int = this match
    case EffectType.Shield   => 6
    case EffectType.Poison   => 6
    case EffectType.Recharge => 5

end EffectType

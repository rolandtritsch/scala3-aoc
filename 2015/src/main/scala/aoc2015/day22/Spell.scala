package aoc2015.day22

/** Spell types available to the player. */
enum Spell(val cost: Int):
  case MagicMissile extends Spell(53)
  case Drain extends Spell(73)
  case Shield extends Spell(113)
  case Poison extends Spell(173)
  case Recharge extends Spell(229)

  /** Returns the effect type this spell creates, if any. */
  def effectType: Option[EffectType] = PartialFunction.condOpt(this) {
    case Spell.Shield   => EffectType.Shield
    case Spell.Poison   => EffectType.Poison
    case Spell.Recharge => EffectType.Recharge
  }

end Spell

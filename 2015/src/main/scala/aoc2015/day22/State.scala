package aoc2015.day22

/** Game state during combat. */
final case class State(
    playerHp: Int,
    playerMana: Int,
    bossHp: Int,
    bossDamage: Int,
    activeEffects: Seq[Effect],
    manaSpent: Int,
    playerTurn: Boolean,
    // Armor from effects active at start of current turn (for boss attack).
    activeArmor: Int = 0,
):

  /** Applies all active effects at start of turn, returns updated state with armor for this turn.
    */
  def applyEffects: State =
    val (totalDamage, totalArmor, totalManaGain) = activeEffects.map(_.applyEffect)
      .foldLeft((0, 0, 0)):
        case ((d, a, m), (dd, aa, mm)) => (d + dd, a + aa, m + mm)
    val newBossHp = bossHp - totalDamage
    val newPlayerMana = playerMana + totalManaGain
    val newEffects = activeEffects.flatMap(_.tick)
    copy(
      bossHp = newBossHp,
      playerMana = newPlayerMana,
      activeEffects = newEffects,
      activeArmor = totalArmor,
    )
  end applyEffects

  /** Checks if boss is dead. */
  def bossDead: Boolean = bossHp <= 0

  /** Checks if player is dead. */
  def playerDead: Boolean = playerHp <= 0

  /** Generates all valid next states after player casts a spell. */
  def playerActions: Seq[State] =
    if playerTurn then
      Spell.values.toSeq.flatMap { spell =>
        // Check if we can afford the spell
        if playerMana >= spell.cost then
          // Check if spell's effect is already active
          val effectActive = spell.effectType.exists(et => activeEffects.exists(_.effectType == et))
          if !effectActive then
            val newMana = playerMana - spell.cost
            val newManaSpent = manaSpent + spell.cost
            // Apply instant effects
            spell match
              case Spell.MagicMissile => Some(copy(
                  bossHp = bossHp - 4,
                  playerMana = newMana,
                  manaSpent = newManaSpent,
                  playerTurn = false,
                  activeArmor = 0,
                ))
              case Spell.Drain => Some(copy(
                  bossHp = bossHp - 2,
                  playerHp = playerHp + 2,
                  playerMana = newMana,
                  manaSpent = newManaSpent,
                  playerTurn = false,
                  activeArmor = 0,
                ))
              case Spell.Shield => Some(copy(
                  playerMana = newMana,
                  manaSpent = newManaSpent,
                  activeEffects = activeEffects :+ Effect(EffectType.Shield, 6),
                  playerTurn = false,
                  activeArmor = 0,
                ))
              case Spell.Poison => Some(copy(
                  playerMana = newMana,
                  manaSpent = newManaSpent,
                  activeEffects = activeEffects :+ Effect(EffectType.Poison, 6),
                  playerTurn = false,
                  activeArmor = 0,
                ))
              case Spell.Recharge => Some(copy(
                  playerMana = newMana,
                  manaSpent = newManaSpent,
                  activeEffects = activeEffects :+ Effect(EffectType.Recharge, 5),
                  playerTurn = false,
                  activeArmor = 0,
                ))
          else None
        else None
      }
    else Seq.empty
  end playerActions

  /** Generates next state after boss attacks. */
  def bossAction: State =
    if !playerTurn && !bossDead then
      val damage = (bossDamage - activeArmor).max(1)
      copy(playerHp = playerHp - damage, playerTurn = true, activeArmor = 0)
    else this

end State

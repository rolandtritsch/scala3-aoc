package aoc2015.day22

/** Shared search helpers for Day 22. */
object Search:

  /** Creates a string key for visited state tracking. */
  def stateKey(state: State): String =
    val effectsKey = state.activeEffects.map(e => s"${e.effectType}-${e.timer}").sorted
      .mkString(",")
    s"${state.playerHp},${state.playerMana},${state.bossHp},${state.activeArmor},$effectsKey,${state
        .playerTurn}"

end Search

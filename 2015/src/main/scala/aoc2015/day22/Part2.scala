package aoc2015.day22

import scala.annotation.tailrec
import scala.collection.mutable

/** Finds the least amount of mana to spend and still win the fight on hard mode. */
def part2(input: Input): Int =
  val startState = State(
    playerHp = 50,
    playerMana = 500,
    bossHp = input.hitPoints,
    bossDamage = input.damage,
    activeEffects = Seq.empty,
    manaSpent = 0,
    playerTurn = true,
  )

  // Dijkstra's algorithm: priority queue ordered by mana spent (min-heap)
  given Ordering[State] = Ordering.by(-_.manaSpent)
  val pq = mutable.PriorityQueue[State]()
  val visited = mutable.Set[String]()
  pq.enqueue(startState)

  @tailrec
  def search(): Int =
    if pq.isEmpty then -1 // Should not reach here
    else
      val state = pq.dequeue()

      // Hard mode: at start of player turn, lose 1 HP before effects
      val afterHardMode =
        if state.playerTurn then state.copy(playerHp = state.playerHp - 1) else state
      if afterHardMode.playerDead then search()
      else
        val afterEffects = afterHardMode.applyEffects

        if afterEffects.bossDead then afterEffects.manaSpent
        else if afterEffects.playerDead then search()
        else
          val nextStates: Seq[State] =
            if afterEffects.playerTurn then afterEffects.playerActions
            else Seq(afterEffects.bossAction)

          for nextState <- nextStates do
            val key = stateKey(nextState)
            if !visited.contains(key) && !nextState.playerDead then
              visited.add(key)
              pq.enqueue(nextState)
            else ()

          search()
  end search

  search()
end part2

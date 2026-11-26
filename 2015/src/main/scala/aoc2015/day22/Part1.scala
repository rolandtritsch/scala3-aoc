package aoc2015.day22

import scala.annotation.tailrec
import scala.collection.mutable

/** Finds the least amount of mana to spend and still win the fight. */
def part1(input: Input): Int =
  val startState = State(
    playerHp = 50,
    playerMana = 500,
    bossHp = input.hitPoints,
    bossDamage = input.damage,
    activeEffects = Seq.empty,
    manaSpent = 0,
    playerTurn = true,
  )

  // Dijkstra's algorithm with cost map
  given Ordering[State] = Ordering.by(-_.manaSpent)
  val pq = mutable.PriorityQueue[State]()
  val bestCost = mutable.Map[String, Int]()
  pq.enqueue(startState)
  bestCost(Search.stateKey(startState)) = 0

  @tailrec
  def search(): Int =
    if pq.isEmpty then -1
    else
      val state = pq.dequeue()
      val key = Search.stateKey(state)
      // Skip if we've found a better path to this state
      if state.manaSpent > bestCost.getOrElse(key, Int.MaxValue) then search()
      else
        val afterEffects = state.applyEffects

        if afterEffects.bossDead then afterEffects.manaSpent
        else if afterEffects.playerDead then search()
        else
          val nextStates: Seq[State] =
            if afterEffects.playerTurn then afterEffects.playerActions
            else Seq(afterEffects.bossAction)

          for nextState <- nextStates do
            if !nextState.playerDead then
              val nextKey = Search.stateKey(nextState)
              val currentBest = bestCost.getOrElse(nextKey, Int.MaxValue)
              if nextState.manaSpent < currentBest then
                bestCost(nextKey) = nextState.manaSpent
                pq.enqueue(nextState)
              else ()
            else ()

          search()
  end search

  search()
end part1

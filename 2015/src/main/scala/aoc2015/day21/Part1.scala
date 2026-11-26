package aoc2015.day21

/** Finds the least amount of gold to spend and still win the fight. */
def part1(input: Input): Int =
  val boss = Combatant(input.hitPoints, input.damage, input.armor)
  val playerBaseHp = 100

  val winningCosts = allEquipmentCombinations.flatMap { items =>
    val (cost, damage, armor) = statsFromEquipment(items)
    val player = Combatant(playerBaseHp, damage, armor)
    Option.when(simulateFight(player, boss) == FightResult.PlayerWins)(cost)
  }

  winningCosts.min
end part1

package aoc2015.day21

/** Finds the most gold you can spend and still lose the fight. */
def part2(input: Input): Int =
  val boss = Combatant(input.hitPoints, input.damage, input.armor)
  val playerBaseHp = 100

  val losingCosts = allEquipmentCombinations.flatMap { items =>
    val (cost, damage, armor) = statsFromEquipment(items)
    val player = Combatant(playerBaseHp, damage, armor)
    Option.when(simulateFight(player, boss) == FightResult.BossWins)(cost)
  }

  losingCosts.max
end part2

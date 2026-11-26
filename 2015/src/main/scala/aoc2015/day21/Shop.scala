package aoc2015.day21

/** Simulate a fight between player and boss. Player attacks first. */
def simulateFight(player: Combatant, boss: Combatant): FightResult =
  @scala.annotation.tailrec
  def loop(pHp: Int, bHp: Int, playerTurn: Boolean): FightResult =
    if pHp <= 0 then FightResult.BossWins
    else if bHp <= 0 then FightResult.PlayerWins
    else if playerTurn then
      val dmg = math.max(1, player.damage - boss.armor)
      loop(pHp, bHp - dmg, false)
    else
      val dmg = math.max(1, boss.damage - player.armor)
      loop(pHp - dmg, bHp, true)
  end loop

  loop(player.hitPoints, boss.hitPoints, true)
end simulateFight

/** All available shop items. */
val shopItems: Seq[Item] = Seq(
  // Weapons (must buy exactly one)
  Item("Dagger", 8, 4, 0, ItemType.Weapon),
  Item("Shortsword", 10, 5, 0, ItemType.Weapon),
  Item("Warhammer", 25, 6, 0, ItemType.Weapon),
  Item("Longsword", 40, 7, 0, ItemType.Weapon),
  Item("Greataxe", 74, 8, 0, ItemType.Weapon),
  // Armor (optional, max one)
  Item("Leather", 13, 0, 1, ItemType.Armor),
  Item("Chainmail", 31, 0, 2, ItemType.Armor),
  Item("Splintmail", 53, 0, 3, ItemType.Armor),
  Item("Bandedmail", 75, 0, 4, ItemType.Armor),
  Item("Platemail", 102, 0, 5, ItemType.Armor),
  // Rings (0-2, at most one of each)
  Item("Damage +1", 25, 1, 0, ItemType.Ring),
  Item("Damage +2", 50, 2, 0, ItemType.Ring),
  Item("Damage +3", 100, 3, 0, ItemType.Ring),
  Item("Defense +1", 20, 0, 1, ItemType.Ring),
  Item("Defense +2", 40, 0, 2, ItemType.Ring),
  Item("Defense +3", 80, 0, 3, ItemType.Ring),
)

/** Generate all valid equipment combinations. */
def allEquipmentCombinations: Seq[Seq[Item]] =
  val weapons = shopItems.filter(_.itemType == ItemType.Weapon)
  val armors = shopItems.filter(_.itemType == ItemType.Armor)
  val rings = shopItems.filter(_.itemType == ItemType.Ring)

  for
    weapon <- weapons
    armor <- Seq(None) ++ armors.map(Some(_))
    ringCount <- 0 to 2
    ringCombo <- rings.combinations(ringCount)
  yield Seq(weapon) ++ armor.toSeq ++ ringCombo
end allEquipmentCombinations

/** Compute total stats from equipment. */
def statsFromEquipment(items: Seq[Item]): (Int, Int, Int) =
  val totalCost = items.map(_.cost).sum
  val totalDamage = items.map(_.damage).sum
  val totalArmor = items.map(_.armor).sum
  (totalCost, totalDamage, totalArmor)

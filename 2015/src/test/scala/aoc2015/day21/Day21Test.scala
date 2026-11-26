package aoc2015.day21

import zio.test.*

object Day21Test extends ZIOSpecDefault:

  def spec: Spec[Any, Any] = suite("Day21")(
    test("simulateFight - example from problem"):
      // Player: 8 HP, 5 damage, 5 armor
      // Boss: 12 HP, 7 damage, 2 armor
      // Player deals 5-2=3, Boss deals 7-5=2
      val player = Combatant(8, 5, 5)
      val boss = Combatant(12, 7, 2)
      assertTrue(simulateFight(player, boss) == FightResult.PlayerWins)
    ,
    test("simulateFight - player loses"):
      val player = Combatant(10, 1, 0)
      val boss = Combatant(10, 10, 0)
      assertTrue(simulateFight(player, boss) == FightResult.BossWins)
    ,
    test("part1 - example equipment selection"):
      // This is a sanity check: we can't easily test the exact example
      // since the problem doesn't give a complete example with items
      // but we can verify the algorithm works on a simplified case
      val input = Input(12, 7, 2) // boss from example
      // With 8 HP player, this would need different player HP
      // We just test that part1 returns a valid cost
      // (this will use the default 100 HP, not the example's 8 HP)
      assertTrue(part1(input) > 0)
    ,
    test("allEquipmentCombinations - generates valid combinations"):
      val combos = allEquipmentCombinations
      // Must have exactly one weapon
      assertTrue(combos.forall(_.count(_.itemType == ItemType.Weapon) == 1))
      // At most one armor
      assertTrue(combos.forall(_.count(_.itemType == ItemType.Armor) <= 1))
      // At most two rings
      assertTrue(combos.forall(_.count(_.itemType == ItemType.Ring) <= 2))
      // No duplicate rings
      assertTrue(combos.forall { items =>
        val rings = items.withFilter(_.itemType == ItemType.Ring).map(_.name)
        rings.distinct.length == rings.length
      })
    ,
    test("statsFromEquipment - computes correct totals"):
      val items = Seq(
        Item("Dagger", 8, 4, 0, ItemType.Weapon),
        Item("Leather", 13, 0, 1, ItemType.Armor),
        Item("Damage +1", 25, 1, 0, ItemType.Ring),
      )
      val (cost, dmg, arm) = statsFromEquipment(items)
      assertTrue(cost == 46, dmg == 5, arm == 1)
    ,
    test("readFile - loads real input"):
      readFile("inputs/Day21.txt")
        .map(input => assertTrue(input.hitPoints == 104, input.damage == 8, input.armor == 1))
    ,
    test("part1 - real input answer"):
      readFile("inputs/Day21.txt").map(input => assertTrue(part1(input) == 78))
    ,
    test("part2 - real input answer"):
      readFile("inputs/Day21.txt").map(input => assertTrue(part2(input) == 148)),
  )
  end spec

end Day21Test

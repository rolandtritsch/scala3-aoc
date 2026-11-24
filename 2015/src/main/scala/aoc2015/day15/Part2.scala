package aoc2015.day15

/** The exact calorie total a part-two cookie must have to be considered. */
val targetCalories: Int = 500

/** The highest possible cookie score whose total calorie count is exactly `targetCalories`.
  *
  * Every way to allocate exactly `teaspoons` teaspoons across the ingredients is generated, the
  * allocations whose calorie count differs from `targetCalories` are discarded, and the maximum
  * score among the rest is returned. Calories gate which cookies are eligible but are still ignored
  * by the score itself.
  */
def part2(ingredients: Seq[Ingredient]): Int = compositions(ingredients.size, teaspoons)
  .withFilter(amounts => calories(ingredients, amounts) == targetCalories)
  .map(amounts => score(ingredients, amounts)).max

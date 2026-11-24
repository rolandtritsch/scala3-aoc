package aoc2015.day15

/** The highest possible cookie score with exactly `teaspoons` of ingredients.
  *
  * Every way to allocate the teaspoons across the ingredients is considered, the score of each
  * allocation is computed, and the maximum score is returned. The answer ignores calories.
  */
def part1(ingredients: Seq[Ingredient]): Int = compositions(ingredients.size, teaspoons)
  .map(amounts => score(ingredients, amounts)).max

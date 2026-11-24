package aoc2015.day15

import aoc2015.util.AocError

/** One baking ingredient and the contribution a single teaspoon makes to each cookie property.
  *
  * A recipe has exactly 100 teaspoons to split across the listed ingredients. Each property
  * (capacity, durability, flavor, texture, and calories) sums `teaspoons * property` over every
  * ingredient; part one ignores calories. Property values may be negative, which the puzzle floors
  * to zero when combining them into a score.
  *
  * @param name
  *   the ingredient's name.
  * @param capacity
  *   the absorbency contributed per teaspoon.
  * @param durability
  *   the structural integrity contributed per teaspoon.
  * @param flavor
  *   the taste contributed per teaspoon.
  * @param texture
  *   the mouthfeel contributed per teaspoon.
  * @param calories
  *   the calories contributed per teaspoon.
  */
final case class Ingredient(
    name: String,
    capacity: Int,
    durability: Int,
    flavor: Int,
    texture: Int,
    calories: Int,
)

object Ingredient:

  private val Line =
    """(.+): capacity (-?\d+), durability (-?\d+), flavor (-?\d+), texture (-?\d+), calories (-?\d+)"""
      .r

  /** Parse one `Name: capacity C, durability D, flavor F, texture T, calories K` line.
    *
    * Properties are signed integers because capacity, durability, flavor, and texture can be
    * negative (a negative total later floors to zero). The name is the leading text before the
    * `: capacity` phrase.
    *
    * @param filename
    *   the resource name, used for error messages.
    * @param line
    *   the raw input line.
    * @param lineNumber
    *   the one-based line number, used for error messages.
    * @return
    *   the parsed ingredient, or a parse error.
    */
  def parse(filename: String, line: String, lineNumber: Int = 1): Either[AocError, Ingredient] =
    def invalid(reason: String): Left[AocError, Nothing] =
      Left(AocError.ParseError(filename, s"line $lineNumber: $reason: $line"))

    line.trim match
      case Line(name, capacityText, durabilityText, flavorText, textureText, caloriesText) => (
          capacityText.toIntOption,
          durabilityText.toIntOption,
          flavorText.toIntOption,
          textureText.toIntOption,
          caloriesText.toIntOption,
        ) match
          case (Some(capacity), Some(durability), Some(flavor), Some(texture), Some(calories)) =>
            Right(Ingredient(name, capacity, durability, flavor, texture, calories))
          case _ => invalid("properties must be integers")
      case _ =>
        invalid("expected \"Name: capacity C, durability D, flavor F, texture T, calories K\"")
    end match
  end parse

end Ingredient

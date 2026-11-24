package aoc2015.day15

import aoc2015.util.AocError
import aoc2015.util.parseLines
import aoc2015.util.readResource
import zio.*

/** Load and validate Day15 input for the solution.
  *
  * The payload is the parsed list of ingredients. At least one ingredient must be present so a
  * cookie has a composition to score, and every ingredient must have a unique name.
  */
def readFile(filename: String): ZIO[Any, AocError, Seq[Ingredient]] = readResource(filename) {
  (name, input) =>
    parseLines(name, input)(Ingredient.parse).flatMap { ingredients =>
      val names = ingredients.map(_.name)
      if ingredients.isEmpty then
        Left(AocError.ValidationError(s"$name: at least one ingredient is required"))
      else if names.distinct.size != names.size then
        Left(AocError.ValidationError(s"$name: ingredient names must all be distinct"))
      else Right(ingredients)
    }
}
end readFile

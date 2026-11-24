package aoc2015.day15

import aoc2015.util.AocError
import zio.test.*

object Day15Test extends ZIOSpecDefault:

  def spec: Spec[Any, Any] = suite("Day15")(
    test("parse - a full ingredient profile with a negative property"):
      assertTrue(
        Ingredient
          .parse("sample", "Sprinkles: capacity 2, durability 0, flavor -2, texture 0, calories 3")
          .map(i => (i.name, i.capacity, i.durability, i.flavor, i.texture, i.calories)) ==
          Right(("Sprinkles", 2, 0, -2, 0, 3))
      )
    ,
    test("parse - rejects malformed lines"):
      assertTrue(
        Seq(
          "Sprinkles capacity 2, durability 0, flavor -2, texture 0, calories 3",
          "Sprinkles: capacity 2, durability 0, flavor -2, texture 0",
          "Sprinkles: capacity 2, durability 0, flavor -2, texture 0, calories abc",
          "Sprinkles: capacity two, durability 0, flavor -2, texture 0, calories 3",
        ).forall(line => Ingredient.parse("sample", line).isLeft)
      )
    ,
    test("score - official two-ingredient example"):
      readFile("inputs/Day15Boundary.txt")
        .map(ingredients => assertTrue(score(ingredients, Seq(44, 56)) == 62842880))
    ,
    test("score - a negative property total floors at zero"):
      readFile("inputs/Day15Boundary.txt").map { ingredients =>
        assertTrue(score(ingredients, Seq(100, 0)) == 0, score(ingredients, Seq(0, 100)) == 0)
      }
    ,
    test("part1 - official sample best score of 62842880"):
      readFile("inputs/Day15Boundary.txt")
        .map(ingredients => assertTrue(part1(ingredients) == 62842880))
    ,
    test("part1 - real input answer"):
      readFile("inputs/Day15.txt").map(ingredients => assertTrue(part1(ingredients) == 21367368))
    ,
    test("readFile - malformed line reports the offending line"):
      readFile("inputs/Day15Invalid.txt").either.map(result =>
        assertTrue(result.left.exists {
          case AocError.ParseError("inputs/Day15Invalid.txt", message) => message.contains("line 2")
          case _                                                       => false
        })
      )
    ,
    test("readFile - real input loads as ingredients"):
      readFile("inputs/Day15.txt").map(ingredients => assertTrue(ingredients.nonEmpty)),
  )
  end spec

end Day15Test

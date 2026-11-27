package aoc2016.day10

import zio.test.*

object Day10Test extends ZIOSpecDefault:

  private val officialExample = """value 5 goes to bot 2
                                  |bot 2 gives low to bot 1 and high to bot 0
                                  |value 3 goes to bot 1
                                  |bot 1 gives low to output 1 and high to bot 0
                                  |bot 0 gives low to output 2 and high to output 0
                                  |value 2 goes to bot 2""".stripMargin

  def spec: Spec[Any, Any] = suite("Day10")(
    test("parse - maps values, rules and destinations"):
      assertTrue(
        parse("sample", officialExample) == Right(Factory(
          initialChips = Map(2 -> Seq(5, 2), 1 -> Seq(3)),
          rules = Map(
            2 -> BotRule(Destination.Bot(1), Destination.Bot(0)),
            1 -> BotRule(Destination.Output(1), Destination.Bot(0)),
            0 -> BotRule(Destination.Output(2), Destination.Output(0)),
          ),
        ))
      )
    ,
    test("parse - rejects malformed and overflowing instructions"):
      assertTrue(
        Seq(
          "",
          "value -1 goes to bot 2",
          "value 5 goes nowhere",
          "bot 2 gives low to nowhere 1 and high to bot 0",
          "value 2147483648 goes to bot 2",
          "bot 2147483648 gives low to bot 1 and high to bot 0",
        ).forall(input => parse("sample", input).isLeft)
      )
    ,
    test("parse - rejects duplicate rules and three initial chips"):
      val duplicateRule = """bot 2 gives low to bot 1 and high to bot 0
                            |bot 2 gives low to output 1 and high to output 0""".stripMargin
      val threeChips = """value 1 goes to bot 2
                         |value 2 goes to bot 2
                         |value 3 goes to bot 2""".stripMargin
      assertTrue(parse("sample", duplicateRule).isLeft && parse("sample", threeChips).isLeft)
    ,
    test("part1 - official example"):
      assertTrue(parse("sample", officialExample).map(findBotComparing(_, 2, 5)) == Right(Some(2)))
    ,
    test("part2 - official example"):
      assertTrue(parse("sample", officialExample).map(part2) == Right(Some(30)))
    ,
    test("readFile and part1 - personal input"):
      readFile("inputs/Day10.txt").map(input => assertTrue(part1(input) == Some(47)))
    ,
    test("readFile and part2 - personal input"):
      readFile("inputs/Day10.txt").map(input => assertTrue(part2(input) == Some(2666))),
  )
  end spec

end Day10Test

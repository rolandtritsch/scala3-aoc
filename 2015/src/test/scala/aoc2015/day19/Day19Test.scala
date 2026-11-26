package aoc2015.day19

import zio.test.*

object Day19Test extends ZIOSpecDefault:

  def spec: Spec[Any, Any] = suite("Day19")(
    test("parseRule - parses valid rule"):
      assertTrue(parseRule("test", "H => HO") == Right(Replacement("H", "HO")))
    ,
    test("parseRule - rejects invalid format"):
      assertTrue(parseRule("test", "H > HO").isLeft)
    ,
    test("readFile - loads example input"):
      val input = """H => HO
H => OH
O => HH

HOH"""
      val parsed = parseExample(input)
      assertTrue(parsed.replacements.length == 3 && parsed.medicine == "HOH")
    ,
    test("part1 - example from problem (HOH -> 4 distinct molecules)"):
      val input = Input(
        replacements = Seq(Replacement("H", "HO"), Replacement("H", "OH"), Replacement("O", "HH")),
        medicine = "HOH",
      )
      assertTrue(part1(input) == 4)
    ,
    test("part1 - HOHOHO example (7 distinct molecules)"):
      val input = Input(
        replacements = Seq(Replacement("H", "HO"), Replacement("H", "OH"), Replacement("O", "HH")),
        medicine = "HOHOHO",
      )
      assertTrue(part1(input) == 7)
    ,
    test("readFile - loads real input"):
      readFile("inputs/Day19.txt")
        .map(input => assertTrue(input.replacements.nonEmpty && input.medicine.nonEmpty))
    ,
    test("part1 - real input answer"):
      readFile("inputs/Day19.txt").map(input => assertTrue(part1(input) == 509)),
  )
  end spec

  private def parseExample(input: String): Input =
    val lines = input.linesIterator.filter(_.trim.nonEmpty).toSeq
    val (ruleLines, moleculeLines) = lines.splitAt(lines.length - 1)
    val rules = ruleLines.flatMap { line =>
      PartialFunction
        .condOpt(line.split(" => ").toSeq) { case Seq(from, to) => Replacement(from, to) }
    }
    Input(rules, moleculeLines.head.trim)
  end parseExample

end Day19Test

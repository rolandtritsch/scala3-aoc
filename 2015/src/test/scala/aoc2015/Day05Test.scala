package aoc2015

import aoc2015.Day05.*

class Day05Test extends munit.ScalaCheckSuite:

  test("Day05 - part1 - official samples"):
    assertEquals(isNice("ugknbfddgicrmopn"), true)
    assertEquals(isNice("aaa"), true)
    assertEquals(isNice("jchzalrnumimnmhp"), false)
    assertEquals(isNice("haegwjzuvuyypxyu"), false)
    assertEquals(isNice("dvszwmarrgswjxmb"), false)

  test("Day05 - part1 - official samples together"):
    assertEquals(
      part1(
        Seq("ugknbfddgicrmopn", "aaa", "jchzalrnumimnmhp", "haegwjzuvuyypxyu", "dvszwmarrgswjxmb")
      ),
      2,
    )

  test("Day05 - hasThreeVowels - boundary"):
    assertEquals(hasThreeVowels(""), false)
    assertEquals(hasThreeVowels("ab"), false)
    assertEquals(hasThreeVowels("aei"), true)
    assertEquals(hasThreeVowels("aeiouaeiouaeiou"), true)

  test("Day05 - hasDoubleLetter - boundary"):
    assertEquals(hasDoubleLetter(""), false)
    assertEquals(hasDoubleLetter("a"), false)
    assertEquals(hasDoubleLetter("xx"), true)
    assertEquals(hasDoubleLetter("aaa"), true)
    assertEquals(hasDoubleLetter("abab"), false)

  test("Day05 - hasNoForbidden - boundary"):
    assertEquals(hasNoForbidden("aei"), true)
    assertEquals(hasNoForbidden("ab"), false)
    assertEquals(hasNoForbidden("cd"), false)
    assertEquals(hasNoForbidden("pq"), false)
    assertEquals(hasNoForbidden("xy"), false)

  test("Day05 - isNice - empty input"):
    assertEquals(isNice(""), false)

  test("Day05 - isNice - overlap between rules is allowed"):
    assertEquals(isNice("aaa"), true)
    assertEquals(isNice("aeiouxx"), true)

  test("Day05 - isNice - forbidden substring wins over other rules"):
    assertEquals(isNice("aabbccddee"), false)

  test("Day05 - readFile"):
    val obtained = readFile("inputs/Day05.txt")
    assertEquals(obtained.length, 1000)
    assertEquals(obtained.head, "zgsnvdmlfuplrubt")

  test("Day05 - part1 - real input"):
    val input = readFile("inputs/Day05.txt")
    assertEquals(part1(input), 238)

end Day05Test

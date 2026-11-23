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

  test("Day05 - part2 - official samples"):
    assertEquals(isNicePart2("qjhvhtzxzqqjkmpb"), true)
    assertEquals(isNicePart2("xxyxx"), true)
    assertEquals(isNicePart2("uurcxstgmygtbstg"), false)
    assertEquals(isNicePart2("ieodomkazucvgmuy"), false)

  test("Day05 - part2 - official samples together"):
    assertEquals(part2(Seq("qjhvhtzxzqqjkmpb", "xxyxx", "uurcxstgmygtbstg", "ieodomkazucvgmuy")), 2)

  test("Day05 - hasRepeatingPair - boundary"):
    assertEquals(hasRepeatingPair(""), false)
    assertEquals(hasRepeatingPair("a"), false)
    assertEquals(hasRepeatingPair("aa"), false)
    assertEquals(hasRepeatingPair("aaa"), false)
    assertEquals(hasRepeatingPair("aaaa"), true)
    assertEquals(hasRepeatingPair("xyxy"), true)
    assertEquals(hasRepeatingPair("aabcdefgaa"), true)
    assertEquals(hasRepeatingPair("qjhvhtzxzqqjkmpb"), true)

  test("Day05 - hasRepeatWithGap - boundary"):
    assertEquals(hasRepeatWithGap(""), false)
    assertEquals(hasRepeatWithGap("a"), false)
    assertEquals(hasRepeatWithGap("ab"), false)
    assertEquals(hasRepeatWithGap("xyx"), true)
    assertEquals(hasRepeatWithGap("aaa"), true)
    assertEquals(hasRepeatWithGap("abcdefeghi"), true)
    assertEquals(hasRepeatWithGap("uurcxstgmygtbstg"), false)

  test("Day05 - isNicePart2 - empty input"):
    assertEquals(isNicePart2(""), false)

  test("Day05 - isNicePart2 - overlap between rules is allowed"):
    assertEquals(isNicePart2("xxyxx"), true)

  test("Day05 - part2 - real input"):
    val input = readFile("inputs/Day05.txt")
    val obtained = part2(input)
    assert(clue(obtained) > 0)

end Day05Test

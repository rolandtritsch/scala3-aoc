package aoc2015

import aoc2015.Day05.*
import zio.*
import zio.test.*

object Day05Test extends ZIOSpecDefault:

  def spec: Spec[Any, Any] = suite("Day05")(
    test("part1 - official samples"):
      assertTrue(
        isNice("ugknbfddgicrmopn"),
        isNice("aaa"),
        !isNice("jchzalrnumimnmhp"),
        !isNice("haegwjzuvuyypxyu"),
        !isNice("dvszwmarrgswjxmb"),
      )
    ,
    test("part1 - official samples together"):
      assertTrue(
        part1(
          Seq("ugknbfddgicrmopn", "aaa", "jchzalrnumimnmhp", "haegwjzuvuyypxyu", "dvszwmarrgswjxmb")
        ) == 2
      )
    ,
    test("hasThreeVowels - boundary"):
      assertTrue(
        !hasThreeVowels(""),
        !hasThreeVowels("ab"),
        hasThreeVowels("aei"),
        hasThreeVowels("aeiouaeiouaeiou"),
      )
    ,
    test("hasDoubleLetter - boundary"):
      assertTrue(
        !hasDoubleLetter(""),
        !hasDoubleLetter("a"),
        hasDoubleLetter("xx"),
        hasDoubleLetter("aaa"),
        !hasDoubleLetter("abab"),
      )
    ,
    test("hasNoForbidden - boundary"):
      assertTrue(
        hasNoForbidden("aei"),
        !hasNoForbidden("ab"),
        !hasNoForbidden("cd"),
        !hasNoForbidden("pq"),
        !hasNoForbidden("xy"),
      )
    ,
    test("isNice - empty input"):
      assertTrue(!isNice(""))
    ,
    test("isNice - overlap between rules is allowed"):
      assertTrue(isNice("aaa"), isNice("aeiouxx"))
    ,
    test("isNice - forbidden substring wins over other rules"):
      assertTrue(!isNice("aabbccddee"))
    ,
    test("readFile"):
      readFile("inputs/Day05.txt")
        .map(obtained => assertTrue(obtained.length == 1000, obtained.head == "zgsnvdmlfuplrubt"))
    ,
    test("part1 - real input"):
      readFile("inputs/Day05.txt").map(input => assertTrue(part1(input) == 238))
    ,
    test("part2 - official samples"):
      assertTrue(
        isNicePart2("qjhvhtzxzqqjkmpb"),
        isNicePart2("xxyxx"),
        !isNicePart2("uurcxstgmygtbstg"),
        !isNicePart2("ieodomkazucvgmuy"),
      )
    ,
    test("part2 - official samples together"):
      assertTrue(
        part2(Seq("qjhvhtzxzqqjkmpb", "xxyxx", "uurcxstgmygtbstg", "ieodomkazucvgmuy")) == 2
      )
    ,
    test("hasRepeatingPair - boundary"):
      assertTrue(
        !hasRepeatingPair(""),
        !hasRepeatingPair("a"),
        !hasRepeatingPair("aa"),
        !hasRepeatingPair("aaa"),
        hasRepeatingPair("aaaa"),
        hasRepeatingPair("xyxy"),
        hasRepeatingPair("aabcdefgaa"),
        hasRepeatingPair("qjhvhtzxzqqjkmpb"),
      )
    ,
    test("hasRepeatWithGap - boundary"):
      assertTrue(
        !hasRepeatWithGap(""),
        !hasRepeatWithGap("a"),
        !hasRepeatWithGap("ab"),
        hasRepeatWithGap("xyx"),
        hasRepeatWithGap("aaa"),
        hasRepeatWithGap("abcdefeghi"),
        !hasRepeatWithGap("uurcxstgmygtbstg"),
      )
    ,
    test("isNicePart2 - empty input"):
      assertTrue(!isNicePart2(""))
    ,
    test("isNicePart2 - overlap between rules is allowed"):
      assertTrue(isNicePart2("xxyxx"))
    ,
    test("part2 - real input"):
      readFile("inputs/Day05.txt").map(input => assertTrue(part2(input) > 0)),
  )

end Day05Test

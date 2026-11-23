package aoc2015.day05

import aoc2015.util.AocError
import zio.test.*

object Day05Test extends ZIOSpecDefault:

  def spec: Spec[Any, Any] = suite("Day05")(
    test("parse - lowercase text and surrounding whitespace"):
      assertTrue(
        parse("sample", " aaa ") == Right("aaa"),
        Seq("", "AAA", "abc1", "ab cd", "é").forall(line => parse("sample", line).isLeft),
      )
    ,
    test("readFile - invalid word includes its location"):
      readFile("inputs/Day05Invalid.txt").either.map(result =>
        assertTrue(result.left.exists {
          case AocError.ParseError("inputs/Day05Invalid.txt", message) => message.contains("line 2")
          case _                                                       => false
        })
      )
    ,
    test("hasRepeatingPair - agrees with exhaustive short-word reference"):
      val words = (0 to 8).flatMap(length =>
        (0 until (1 << length)).map { bits =>
          (0 until length).map(i => if (bits & (1 << i)) == 0 then 'a' else 'b').mkString
        }
      )
      assertTrue(words.forall { word =>
        val reference = (0 until word.length - 1).exists(i =>
          (i + 2 until word.length - 1)
            .exists(j => word.substring(i, i + 2) == word.substring(j, j + 2))
        )
        hasRepeatingPair(word) == reference
      })
    ,
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
      readFile("inputs/Day05.txt").map(obtained =>
        assertTrue(obtained.length == 1000, obtained.headOption.contains("zgsnvdmlfuplrubt"))
      )
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
      readFile("inputs/Day05.txt").map(input => assertTrue(part2(input) == 69)),
  )

end Day05Test

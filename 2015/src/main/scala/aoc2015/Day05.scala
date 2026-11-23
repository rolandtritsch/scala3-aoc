package aoc2015

import zio.*

/** Day05 - Doesn't He Have Intern-Elves For This?
  *
  * Santa has a list of strings, each of which is either naughty or nice.
  *
  * Part 1 rules — a string is nice exactly when all three of the following rules hold, applied to
  * the lowercase characters of the string:
  *
  *   - it contains at least three vowels (`aeiou` only);
  *   - it contains at least one letter that appears twice in a row, such as `xx`, `abcdde`, or one
  *     of the pairs in `aabbccdd`;
  *   - it does not contain any of the substrings `ab`, `cd`, `pq`, or `xy`, even if they would
  *     count toward another rule.
  *
  * The rules apply independently, so the characters used by one rule may overlap with those used by
  * another (for example `aaa` satisfies both the vowel and the double-letter rules).
  *
  * Part 2 rules — a string is nice exactly when both of the following rules hold:
  *
  *   - it contains a pair of any two letters that appears at least twice in the string without
  *     overlapping, such as `xyxy` (`xy`) or `aabcdefgaa` (`aa`), but not like `aaa` (`aa`, but it
  *     overlaps);
  *   - it contains at least one letter which repeats with exactly one letter between them, such as
  *     `xyx`, `abcdefeghi` (`efe`), or even `aaa`.
  *
  * part1: the number of nice strings in the list under the three original rules.
  *
  * part2: the number of nice strings in the list under the two new rules.
  */
object Day05:

  private val vowels: Set[Char] = "aeiou".toSet
  private val forbidden: Set[String] = Set("ab", "cd", "pq", "xy")

  /** @return
    *   the individual strings read from the given resource file, one per non-empty line, with
    *   surrounding whitespace removed, or an [[AocError]] if the resource is unreadable.
    */
  def readFile(filename: String): ZIO[Any, AocError, Seq[String]] =
    import scala.io.Source

    ZIO.acquireReleaseWith(
      ZIO.attempt(Source.fromResource(filename)).mapError(AocError.FileReadError(filename, _))
    )(source => ZIO.succeed(source.close())) { source =>
      ZIO.attempt(source.getLines().map(_.trim).filter(_.nonEmpty).toSeq)
        .mapError(AocError.FileReadError(filename, _))
    }
  end readFile

  /** @return true if `s` contains at least three of the vowels `aeiou`. */
  def hasThreeVowels(s: String): Boolean = s.count(vowels.contains) >= 3
  end hasThreeVowels

  /** @return true if `s` contains some letter that appears twice in a row. */
  def hasDoubleLetter(s: String): Boolean = s.sliding(2)
    .exists(pair => pair.length == 2 && pair(0) == pair(1))
  end hasDoubleLetter

  /** @return
    *   true if `s` does not contain any of the forbidden substrings `ab`, `cd`, `pq`, or `xy`.
    */
  def hasNoForbidden(s: String): Boolean = !forbidden.exists(s.contains)
  end hasNoForbidden

  /** @return true if `s` satisfies all three niceness rules. */
  def isNice(s: String): Boolean = hasThreeVowels(s) && hasDoubleLetter(s) && hasNoForbidden(s)
  end isNice

  /** @return
    *   true if `s` contains some pair of two letters that appears at least twice without
    *   overlapping. Two occurrences of the same pair must be separated by at least one character so
    *   they do not share a letter.
    */
  def hasRepeatingPair(s: String): Boolean =
    val pairs = s.sliding(2).toSeq
    pairs.indices.exists(i => pairs.indices.drop(i + 2).exists(j => pairs(i) == pairs(j)))
  end hasRepeatingPair

  /** @return
    *   true if `s` contains at least one letter which repeats with exactly one letter between them
    *   (i.e. a three-character sliding window where the first and last characters are equal).
    */
  def hasRepeatWithGap(s: String): Boolean = s.sliding(3)
    .exists(triple => triple.length == 3 && triple(0) == triple(2))
  end hasRepeatWithGap

  /** @return true if `s` satisfies both part-2 niceness rules. */
  def isNicePart2(s: String): Boolean = hasRepeatingPair(s) && hasRepeatWithGap(s)
  end isNicePart2

  /** @return
    *   the number of nice strings in `strings`: those containing at least three vowels, at least
    *   one doubled letter, and none of the forbidden substrings. Each string is inspected a
    *   constant number of times, so this runs in O(n) time and O(1) extra space for n input
    *   strings.
    */
  def part1(strings: Seq[String]): Int = strings.count(isNice)
  end part1

  /** @return
    *   the number of nice strings in `strings` under the two revised rules: containing at least one
    *   non-overlapping repeating pair and at least one repeated letter with exactly one letter
    *   between. Each string is inspected a constant number of times, so this runs in O(n * L²) time
    *   and O(L) extra space for n input strings of average length L.
    */
  def part2(strings: Seq[String]): Int = strings.count(isNicePart2)
  end part2

end Day05

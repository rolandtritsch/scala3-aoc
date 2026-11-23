package aoc2015

import com.typesafe.scalalogging.Logger

/** Day05 - Doesn't He Have Intern-Elves For This?
  *
  * Santa has a list of strings, each of which is either naughty or nice. A string is nice exactly
  * when all three of the following rules hold, applied to the lowercase characters of the string:
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
  * part1: the number of nice strings in the list.
  */
object Day05:

  val logger: Logger = Logger(this.getClass.getName)

  private val vowels: Set[Char] = "aeiou".toSet
  private val forbidden: Set[String] = Set("ab", "cd", "pq", "xy")

  /** @return
    *   the individual strings read from the given resource file, one per non-empty line, with
    *   surrounding whitespace removed.
    */
  def readFile(filename: String): Seq[String] =
    import scala.io.Source

    require(filename.nonEmpty, "filename.nonEmpty")
    logger.debug(s"filename: ${filename}")

    val source = Source.fromResource(filename)
    try source.getLines().map(_.trim).filter(_.nonEmpty).toSeq
    finally source.close()
    end try
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
    *   the number of nice strings in `strings`: those containing at least three vowels, at least
    *   one doubled letter, and none of the forbidden substrings. Each string is inspected a
    *   constant number of times, so this runs in O(n) time and O(1) extra space for n input
    *   strings.
    */
  def part1(strings: Seq[String]): Int =
    logger.debug(s"strings count: ${strings.length}")

    strings.count(isNice)
  end part1

end Day05

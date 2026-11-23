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

  private val vowels: String = "aeiou"
  private val forbidden: Seq[String] = Seq("ab", "cd", "pq", "xy")

  /** Strings are the domain here. Blank lines are ignored; non-empty lines must be lowercase words.
    */
  def parse(filename: String, line: String, lineNumber: Int = 1): Either[AocError, String] =
    val word = line.trim
    if word.nonEmpty && word.forall(c => c >= 'a' && c <= 'z') then Right(word)
    else
      Left(
        AocError
          .ParseError(filename, s"line $lineNumber: expected a non-empty lowercase word: $line")
      )
    end if

  end parse

  def readFile(filename: String): ZIO[Any, AocError, Seq[String]] =
    import scala.io.Source

    ZIO.acquireReleaseWith(
      ZIO.attempt(Source.fromResource(filename)).mapError(AocError.FileReadError(filename, _))
    )(source => ZIO.succeed(source.close())) { source =>
      for
        _ <- ZIO.logDebug(s"filename: $filename")
        lines <- ZIO.attempt(source.getLines().toSeq).mapError(AocError.FileReadError(filename, _))
        words <- ZIO.foreach(lines.zipWithIndex.filter(_._1.trim.nonEmpty)) { (line, index) =>
          for
            _ <- ZIO.logDebug(s"line ${index + 1}: $line")
            word <- ZIO.fromEither(parse(filename, line, index + 1))
            _ <- ZIO.logDebug(s"parsed: $word")
          yield word
        }
      yield words
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

  /** Remember each pair's earliest index so overlapping occurrences cannot hide a later valid
    * match. A Map provides efficient lookup by pair instead of comparing every pair of positions.
    */
  def hasRepeatingPair(s: String): Boolean =
    @scala.annotation.tailrec
    def search(index: Int, firstSeen: Map[String, Int]): Boolean =
      if index >= s.length - 1 then false
      else
        val pair = s.substring(index, index + 2)
        firstSeen.get(pair) match
          case Some(first) if index - first >= 2 => true
          case Some(_)                           => search(index + 1, firstSeen)
          case None => search(index + 1, firstSeen.updated(pair, index))

    search(0, Map.empty)

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

  /** Count words satisfying all three original niceness rules. Work is linear in total text length.
    */
  def part1(strings: Seq[String]): Int = strings.count(isNice)

  /** Count words satisfying both revised rules. Pair lookups avoid a quadratic pair comparison. */
  def part2(strings: Seq[String]): Int = strings.count(isNicePart2)

end Day05

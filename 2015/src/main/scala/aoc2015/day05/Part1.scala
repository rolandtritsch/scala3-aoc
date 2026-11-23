package aoc2015.day05

private val vowels: String = "aeiou"

private val forbidden: Seq[String] = Seq("ab", "cd", "pq", "xy")

/** @return true if `s` contains at least three of the vowels `aeiou`. */
def hasThreeVowels(s: String): Boolean = s.count(vowels.contains) >= 3

/** @return true if `s` contains some letter that appears twice in a row. */
def hasDoubleLetter(s: String): Boolean = s.sliding(2)
  .exists(pair => pair.length == 2 && pair(0) == pair(1))

/** @return
  *   true if `s` does not contain any of the forbidden substrings `ab`, `cd`, `pq`, or `xy`.
  */
def hasNoForbidden(s: String): Boolean = !forbidden.exists(s.contains)

/** @return true if `s` satisfies all three niceness rules. */
def isNice(s: String): Boolean = hasThreeVowels(s) && hasDoubleLetter(s) && hasNoForbidden(s)

/** Count words satisfying all three original niceness rules. Work is linear in total text length.
  */
def part1(strings: Seq[String]): Int = strings.count(isNice)

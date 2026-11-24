package aoc2015.day08

/** The total number of characters in the escaped re-encoding minus the number of characters of
  * code.
  *
  * Re-encoding a literal escapes every backslash and double quote and wraps the result in a fresh
  * pair of surrounding double quotes. For every literal, the answer adds the length of that
  * re-encoding and subtracts the original code length.
  */
def part2(literals: Seq[StringLiteral]): Int = literals
  .foldLeft(0)((total, literal) => total + encodedLength(literal) - literal.codeLength)

/** The length of one literal's escaped re-encoding, including its surrounding double quotes.
  *
  * Each backslash and double quote in the code becomes a two-character escape, while every other
  * character is copied verbatim, and the whole result is wrapped in two more double quotes.
  */
def encodedLength(literal: StringLiteral): Int = literal.code.iterator
  .count(c => c == '\\' || c == '"') + literal.code.length + 2

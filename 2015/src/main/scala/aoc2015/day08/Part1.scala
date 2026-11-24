package aoc2015.day08

/** The total number of characters of string code minus the number of characters in memory.
  *
  * Each literal contributes its raw `code` length on one side and its decoded `value` length on the
  * other; the puzzle asks for the difference summed over every line.
  */
def part1(literals: Seq[StringLiteral]): Int = literals
  .foldLeft(0)((total, literal) => total + literal.codeLength - literal.memoryLength)

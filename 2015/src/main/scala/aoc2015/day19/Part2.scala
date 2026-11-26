package aoc2015.day19

import scala.annotation.tailrec

/** Find the fewest steps to fabricate the medicine molecule starting from "e". */
def part2(input: Input): Int =
  val molecule = input.medicine

  if molecule.contains("Rn") || molecule.contains("Ar") || molecule.contains("Y") then
    analyticalSolution(molecule)
  else bfsBackwards(input.replacements, molecule)
end part2

/** Analytical solution for the real input structure with Rn/Ar/Y markers. */
private def analyticalSolution(molecule: String): Int =
  val elements = parseElements(molecule)
  val totalElements = elements.size
  val rnCount = elements.count(_ == "Rn")
  val arCount = elements.count(_ == "Ar")
  val yCount = elements.count(_ == "Y")
  totalElements - rnCount - arCount - 2 * yCount - 1
end analyticalSolution

/** BFS working backwards from molecule to "e". */
private def bfsBackwards(replacements: Seq[Replacement], target: String): Int =
  val reverseRules = replacements.map(r => r.to -> r.from)

  @tailrec
  def loop(queue: List[(String, Int)], visited: Set[String]): Int = queue match
    case Nil                      => -1 // Should not happen for valid inputs
    case (current, steps) :: rest =>
      if current == "e" then steps
      else
        val nextStates =
          for
            (to, from) <- reverseRules
            idx <- 0 to current.length - to.length if current.startsWith(to, idx)
            next = current.substring(0, idx) + from + current.substring(idx + to.length)
            if !visited.contains(next)
          yield (next, steps + 1)

        val newVisited = visited ++ nextStates.map(_._1)
        loop(rest ++ nextStates, newVisited)
  end loop

  loop(List((target, 0)), Set(target))
end bfsBackwards

/** Parse a molecule string into a sequence of element symbols. */
private def parseElements(molecule: String): Seq[String] =
  @tailrec
  def loop(i: Int, acc: List[String]): List[String] =
    if i >= molecule.length then acc.reverse
    else
      val ch = molecule(i)
      if ch.isUpper then
        if i + 1 < molecule.length && molecule(i + 1).isLower then
          loop(i + 2, molecule.substring(i, i + 2) :: acc)
        else loop(i + 1, ch.toString :: acc)
      else loop(i + 1, acc)
  end loop

  loop(0, Nil)
end parseElements

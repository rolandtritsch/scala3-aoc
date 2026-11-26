package aoc2015.day19

/** Count distinct molecules that can be created by applying exactly one replacement to the medicine
  * molecule.
  */
def part1(input: Input): Int =
  val rulesByFrom = input.replacements.groupBy(_.from)
  val molecule = input.medicine
  val results = scala.collection.mutable.Set.empty[String]

  for
    (from, replacements) <- rulesByFrom
    idx <- 0 to molecule.length - from.length if molecule.startsWith(from, idx)
    replacement <- replacements
  do
    val newMolecule = molecule.substring(0, idx) + replacement.to +
      molecule.substring(idx + from.length)
    results.add(newMolecule)
  end for

  results.size
end part1

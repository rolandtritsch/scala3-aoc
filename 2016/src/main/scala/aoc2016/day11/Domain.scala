package aoc2016.day11

private[day11] val FloorCount = 4

private[day11] def canonicalPairs(pairs: Seq[PairLocation]): Seq[PairLocation] =
  // Isotope names are interchangeable: only each generator/microchip floor pair
  // affects safety, so sorting collapses symmetric states without merging roles.
  pairs.sortBy(pair => (pair.generatorFloor, pair.microchipFloor))

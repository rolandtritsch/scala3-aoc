package aoc2016.day11

def part2(input: Facility): Option[Int] =
  val firstFloorPair = PairLocation(generatorFloor = 0, microchipFloor = 0)
  // Elerium and dilithium each add a generator/microchip pair on floor one.
  minimumSteps(Facility(canonicalPairs(input.pairs ++ Seq(firstFloorPair, firstFloorPair))))

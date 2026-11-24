package aoc2015.day16

/** The MFCSAM's reading of the gift wrapping and the part-one consistency check against it. */

/** The counts the MFCSAM detected in the gift sample.
  *
  * The reading lists all ten detectable attributes and the number of each found. Part one matches
  * an Aunt Sue whose remembered facts equal these counts exactly.
  */
val tickerTape: Map[Attribute, Int] = Map(
  Attribute.Children -> 3,
  Attribute.Cats -> 7,
  Attribute.Samoyeds -> 2,
  Attribute.Pomeranians -> 3,
  Attribute.Akitas -> 0,
  Attribute.Vizslas -> 0,
  Attribute.Goldfish -> 5,
  Attribute.Trees -> 3,
  Attribute.Cars -> 2,
  Attribute.Perfumes -> 1,
)

/** Whether every fact a Sue remembers equals the corresponding ticker-tape count.
  *
  * Attributes the Sue does not remember are unknown and never contradict the tape, so only the
  * remembered facts are compared.
  */
def consistentWithTape(sue: AuntSue): Boolean = sue.remembered
  .forall((attribute, count) => tickerTape.get(attribute).contains(count))

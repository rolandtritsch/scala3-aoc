package aoc2015.day13

/** The maximum total happiness change over every circular seating arrangement.
  *
  * Every ordering of the people is a valid seating around the circular table, and each adjacent
  * pair (including the wrap-around) contributes both directed happiness changes. The answer is the
  * maximum total happiness over all such circular arrangements.
  */
def part1(preferences: Seq[Preference]): Int =
  val lookup = happinessLookup(preferences)
  seatings(people(preferences)).map(seatingHappiness(_, lookup)).max

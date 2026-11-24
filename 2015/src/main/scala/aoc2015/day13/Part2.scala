package aoc2015.day13

/** The extra attendee introduced in part two: the host, named "Me" in the seating. */
val host: String = "Me"

/** The maximum total happiness change when the apathetic host is seated too.
  *
  * Part two seats the host ("Me") at the table as well. The host neither gains nor loses happiness
  * next to anyone, and every guest is equally indifferent to the host, so each happiness
  * relationship that involves the host contributes zero. The host is therefore added to the guest
  * list and zero-valued relationships are merged into the lookup, after which the part-one maximum
  * over circular seatings applies unchanged.
  */
def part2(preferences: Seq[Preference]): Int =
  val guests = people(preferences)
  val hostRelationships = guests.flatMap(guest => Seq((guest, host) -> 0, (host, guest) -> 0))
  val lookup = happinessLookup(preferences) ++ hostRelationships
  seatings(host +: guests).map(seatingHappiness(_, lookup)).max

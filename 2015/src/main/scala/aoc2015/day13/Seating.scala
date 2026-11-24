package aoc2015.day13

/** Shared helpers that turn a list of preferences into a search over circular seatings. */

/** The distinct people described by the preferences, in the order they first appear. */
def people(preferences: Seq[Preference]): Seq[String] = preferences
  .flatMap(p => Seq(p.person, p.neighbor)).distinct

/** A lookup from each ordered pair of people to the happiness change the first feels next to the
  * second.
  *
  * Preferences are directed and every ordered pair is stated once in the input, so a single value
  * exists for every distinct ordered pair of people.
  */
def happinessLookup(preferences: Seq[Preference]): Map[(String, String), Int] = preferences
  .map(p => (p.person, p.neighbor) -> p.change).toMap

/** The total happiness of one circular seating order.
  *
  * The people are seated clockwise in the given order and the table is circular, so the last person
  * sits next to the first. Each adjacent pair contributes both people's directed happiness change:
  * `a`'s change sitting next to `b` plus `b`'s change sitting next to `a`.
  */
def seatingHappiness(order: Seq[String], lookup: Map[(String, String), Int]): Int =
  val adjacent = order.zip(order.tail) :+ (order.last -> order.head)
  adjacent.map((a, b) => lookup((a, b)) + lookup((b, a))).sum

/** Every distinct circular seating of the given people.
  *
  * Rotations of the same circular order are equivalent, so the first person is held fixed and only
  * the remaining people are permuted. This removes each rotation's duplicates without omitting any
  * distinct arrangement.
  */
def seatings(people: Seq[String]): Seq[Seq[String]] = people match
  case first +: rest => rest.permutations.map(first +: _).toSeq
  case _             => Seq.empty

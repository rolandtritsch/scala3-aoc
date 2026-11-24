package aoc2015.day12

/** The sum of every number in the JSON document, ignoring every object marked "red".
  *
  * Any object that has a property whose value is the string "red" is ignored, together with all of
  * its descendants. Only objects behave this way: a "red" string inside an array still counts the
  * array's other numbers normally.
  */
def part2(json: Json): Long = sumNonRed(json)

/** Sum every number reachable from `json`, descending into arrays and object values, but treating
  * any object whose values include the string "red" as contributing zero.
  */
private def sumNonRed(json: Json): Long = json match
  case Json.Null            => 0L
  case Json.Bool(_)         => 0L
  case Json.Number(value)   => value
  case Json.Text(_)         => 0L
  case Json.Array(elements) => elements.map(sumNonRed).sum
  case Json.Object(entries) =>
    if containsRed(entries) then 0L else entries.map { case (_, value) => sumNonRed(value) }.sum
end sumNonRed

/** True when any of the object's properties has the string "red" as its value. */
private def containsRed(entries: Seq[(String, Json)]): Boolean = entries
  .exists { case (_, Json.Text("red")) => true; case _ => false }

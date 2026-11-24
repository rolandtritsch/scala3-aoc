package aoc2015.day12

/** The sum of every number that appears anywhere in the JSON document.
  *
  * Numbers nested inside arrays or objects all count; strings, booleans, and null contribute
  * nothing. Empty containers therefore sum to zero.
  */
def part1(json: Json): Long = sumNumbers(json)

/** Sum every number reachable from `json`, descending into arrays and object values. */
private def sumNumbers(json: Json): Long = json match
  case Json.Null            => 0L
  case Json.Bool(_)         => 0L
  case Json.Number(value)   => value
  case Json.Text(_)         => 0L
  case Json.Array(elements) => elements.map(sumNumbers).sum
  case Json.Object(entries) => entries.map { case (_, value) => sumNumbers(value) }.sum
end sumNumbers

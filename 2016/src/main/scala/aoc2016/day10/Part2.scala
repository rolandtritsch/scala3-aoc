package aoc2016.day10

def part2(input: Factory): Option[Int] =
  val outputs = simulate(input, None).outputs
  for
    first <- outputs.get(0).flatMap(_.headOption)
    second <- outputs.get(1).flatMap(_.headOption)
    third <- outputs.get(2).flatMap(_.headOption)
  yield first * second * third
end part2

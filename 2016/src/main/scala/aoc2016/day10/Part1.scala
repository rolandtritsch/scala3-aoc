package aoc2016.day10

def part1(input: Factory): Option[Int] = findBotComparing(input, 17, 61)

private[day10] def findBotComparing(
    input: Factory,
    firstTarget: Int,
    secondTarget: Int,
): Option[Int] = simulate(input, Some((firstTarget, secondTarget))).comparingBot

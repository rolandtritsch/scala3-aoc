package aoc2015.day04

import aoc2015.util.AocError

def part2(secretKey: SecretKey): Either[AocError, Int] = findLowest(secretKey, "000000")

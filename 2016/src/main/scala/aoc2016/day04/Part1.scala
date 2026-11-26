package aoc2016.day04

def part1(input: Seq[Room]): Int = input.withFilter(isReal).map(_.sectorId).sum

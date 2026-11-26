package aoc2016.day02

def part1(input: Seq[Seq[Direction]]): String = input
  .scanLeft(Button.Five)((button, instructions) => instructions.foldLeft(button)(move)).drop(1)
  .map(_.label).mkString

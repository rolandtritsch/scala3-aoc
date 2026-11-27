package aoc2016.day10

private enum Instruction:
  case Initial(chip: Int, bot: Int)
  case Rule(bot: Int, rule: BotRule)

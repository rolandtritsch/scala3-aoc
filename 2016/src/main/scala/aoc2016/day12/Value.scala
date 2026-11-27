package aoc2016.day12

enum Value:
  case Literal(number: Int)
  case RegisterValue(register: Register)

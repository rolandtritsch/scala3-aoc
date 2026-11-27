package aoc2016.day12

enum Instruction:
  case Copy(source: Value, target: Register)
  case Increment(register: Register)
  case Decrement(register: Register)
  case JumpNotZero(source: Value, offset: Int)

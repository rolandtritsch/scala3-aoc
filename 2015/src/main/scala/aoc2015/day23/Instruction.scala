package aoc2015.day23

enum Instruction:
  case Hlf(reg: Register)
  case Tpl(reg: Register)
  case Inc(reg: Register)
  case Jmp(offset: Int)
  case Jie(reg: Register, offset: Int)
  case Jio(reg: Register, offset: Int)

end Instruction

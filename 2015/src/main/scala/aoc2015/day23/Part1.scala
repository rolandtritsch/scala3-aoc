package aoc2015.day23

def part1(instructions: Seq[Instruction]): Int = execute(instructions, Registers()).b

def execute(instructions: Seq[Instruction], initial: Registers): Registers =
  @scala.annotation.tailrec
  def loop(pc: Int, regs: Registers): Registers =
    if pc < 0 || pc >= instructions.length then regs
    else
      instructions(pc) match
        case Instruction.Hlf(reg)         => loop(pc + 1, regs.update(reg)(_ / 2))
        case Instruction.Tpl(reg)         => loop(pc + 1, regs.update(reg)(_ * 3))
        case Instruction.Inc(reg)         => loop(pc + 1, regs.update(reg)(_ + 1))
        case Instruction.Jmp(offset)      => loop(pc + offset, regs)
        case Instruction.Jie(reg, offset) =>
          if regs.get(reg) % 2 == 0 then loop(pc + offset, regs) else loop(pc + 1, regs)
        case Instruction.Jio(reg, offset) =>
          if regs.get(reg) == 1 then loop(pc + offset, regs) else loop(pc + 1, regs)
  end loop
  loop(0, initial)
end execute

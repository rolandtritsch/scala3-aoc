package aoc2016.day12

import scala.annotation.tailrec

def execute(instructions: Seq[Instruction], initial: Registers = Registers(0, 0, 0, 0)): Registers =
  @tailrec
  def loop(programCounter: Int, registers: Registers): Registers = instructions
    .lift(programCounter) match
    case None              => registers
    case Some(instruction) =>
      val (nextCounter, nextRegisters) = step(instruction, programCounter, registers)
      loop(nextCounter, nextRegisters)
  end loop

  loop(0, initial)
end execute

private def step(
    instruction: Instruction,
    programCounter: Int,
    registers: Registers,
): (Int, Registers) = instruction match
  case Instruction.Copy(source, target) =>
    (programCounter + 1, update(registers, target, read(registers, source)))
  case Instruction.Increment(register) =>
    (programCounter + 1, update(registers, register, read(registers, register) + 1))
  case Instruction.Decrement(register) =>
    (programCounter + 1, update(registers, register, read(registers, register) - 1))
  case Instruction.JumpNotZero(source, offset) =>
    val nextCounter =
      if read(registers, source) != 0 then programCounter + offset else programCounter + 1
    (nextCounter, registers)
end step

private def read(registers: Registers, source: Value): Int = source match
  case Value.Literal(number)         => number
  case Value.RegisterValue(register) => read(registers, register)

private def read(registers: Registers, register: Register): Int = register match
  case Register.A => registers.a
  case Register.B => registers.b
  case Register.C => registers.c
  case Register.D => registers.d

private def update(registers: Registers, register: Register, value: Int): Registers = register match
  case Register.A => registers.copy(a = value)
  case Register.B => registers.copy(b = value)
  case Register.C => registers.copy(c = value)
  case Register.D => registers.copy(d = value)

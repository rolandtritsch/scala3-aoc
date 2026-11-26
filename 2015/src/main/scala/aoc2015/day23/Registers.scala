package aoc2015.day23

final case class Registers(a: Int = 0, b: Int = 0):

  def get(reg: Register): Int = reg match
    case Register.A => a
    case Register.B => b

  def set(reg: Register, value: Int): Registers = reg match
    case Register.A => copy(a = value)
    case Register.B => copy(b = value)

  def update(reg: Register)(f: Int => Int): Registers = set(reg, f(get(reg)))

end Registers

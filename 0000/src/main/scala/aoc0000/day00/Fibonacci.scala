package aoc0000.day00

/** The Index type guarantees a non-negative iteration count. BigInt keeps the result exact. */
def fib(n: Index): BigInt =
  @scala.annotation.tailrec
  def go(i: Int, a: BigInt, b: BigInt): BigInt =
    if i >= indexValue(n) then a else go(i + 1, b, a + b)

  go(0, 0, 1)

end fib

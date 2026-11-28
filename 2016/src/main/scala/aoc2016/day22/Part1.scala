package aoc2016.day22

/** The part-one answer: the number of viable pairs of nodes `(A, B)`.
  *
  * A pair is viable when `A` is non-empty, `A` and `B` are distinct nodes, and `A`'s used space
  * fits in `B`'s available space. Every ordered pair is counted, so the total is the sum over each
  * non-empty `A` of the other nodes whose `avail` is at least `A.used`. Distinctness is by position
  * in the sequence, since each node appears exactly once.
  */
def part1(input: Input): Int =
  val nodes = input.nodes
  nodes.indices.foldLeft(0) { (total, i) =>
    val a = nodes(i)
    if a.isEmpty then total
    else total + nodes.indices.count(j => j != i && a.used <= nodes(j).avail)
  }
end part1

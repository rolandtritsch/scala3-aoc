package aoc2015.day01

/** @return the floor reached after applying one move. */
private def step(floor: Int, move: Move): Int = move match
  case Move.Up   => floor + 1
  case Move.Down => floor - 1

/** @return
  *   the running floor after each instruction, in order, with the starting floor 0 as the first
  *   element (before any instruction is applied). LazyList lets part2 stop at the first basement
  *   visit. Both parts share this construction, but each call performs its own scan.
  */
private[day01] def floors(instructions: Seq[Move]): LazyList[Int] = instructions.to(LazyList)
  .scanLeft(0)(step)

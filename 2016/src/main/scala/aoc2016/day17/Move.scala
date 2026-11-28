package aoc2016.day17

/** A single step through the vault's grid, in the order the puzzle reads the hash (U, D, L, R). */
enum Move(val character: Char, val dx: Int, val dy: Int):
  case Up extends Move('U', 0, -1)
  case Down extends Move('D', 0, 1)
  case Left extends Move('L', -1, 0)
  case Right extends Move('R', 1, 0)

/** The moves in the order their open/closed flags appear in the first four hash characters. */
private[day17] val MovesByHashIndex: Seq[Move] = Seq(Move.Up, Move.Down, Move.Left, Move.Right)

/** The uppercase path string for a sequence of moves, as hashed alongside the passcode. */
private[day17] def pathString(path: Seq[Move]): String = path.map(_.character).mkString

package aoc2015.day03

import aoc2015.util.Position

private def step(position: Position, direction: Direction): Position = direction match
  case Direction.North => position.copy(y = position.y + 1)
  case Direction.South => position.copy(y = position.y - 1)
  case Direction.East  => position.copy(x = position.x + 1)
  case Direction.West  => position.copy(x = position.x - 1)

private val origin: Position = Position(0, 0)

/** A Set deduplicates houses and supports efficient membership and union. */
private[day03] def visited(directions: Seq[Direction]): Set[Position] = directions
  .foldLeft((Set(origin), origin)) { case ((houses, position), direction) =>
    val next = step(position, direction)
    (houses + next, next)
  }._1

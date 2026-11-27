package aoc2016.day13

import scala.annotation.tailrec
import scala.collection.immutable.Queue

private[day13] val Start = Coordinate(1, 1)

/** Breadth-first search over the open cells, so the first time the target is dequeued its distance
  * is the fewest possible number of steps.
  */
private[day13] def shortestPath(favoriteNumber: Int, target: Coordinate): Option[Int] =
  search(Queue((Start, 0)), Set(Start), favoriteNumber, target)

@tailrec
private def search(
    queue: Queue[(Coordinate, Int)],
    visited: Set[Coordinate],
    favoriteNumber: Int,
    target: Coordinate,
): Option[Int] =
  // A FIFO queue keeps breadth-first order without repeated O(n) Seq appends.
  queue.dequeueOption match
    case None                             => None
    case Some(((cell, steps), remaining)) =>
      if cell == target then Some(steps)
      else
        val next = neighbours(cell).filter(open => isOpen(favoriteNumber, open)).filterNot(visited)
        search(
          remaining.enqueueAll(next.map(_ -> (steps + 1))),
          visited ++ next,
          favoriteNumber,
          target,
        )
end search

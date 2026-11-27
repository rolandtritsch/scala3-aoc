package aoc2016.day13

import scala.annotation.tailrec
import scala.collection.immutable.Queue

/** The number of distinct locations reachable in at most fifty steps, including the start. */
def part2(input: Input): Int = reachableWithin(input.favoriteNumber, 50)

/** Breadth-first count of every open cell whose shortest distance from the start is at most
  * maxSteps. The start is counted at distance zero.
  */
private def reachableWithin(favoriteNumber: Int, maxSteps: Int): Int =
  count(Queue((Start, 0)), Set(Start), favoriteNumber, maxSteps)

@tailrec
private def count(
    queue: Queue[(Coordinate, Int)],
    visited: Set[Coordinate],
    favoriteNumber: Int,
    maxSteps: Int,
): Int =
  // A FIFO queue keeps breadth-first order, so cells are first reached by a shortest path.
  queue.dequeueOption match
    case None                             => visited.size
    case Some(((cell, steps), remaining)) =>
      if steps >= maxSteps then count(remaining, visited, favoriteNumber, maxSteps)
      else
        val next = neighbours(cell).filter(open => isOpen(favoriteNumber, open)).filterNot(visited)
        count(
          remaining.enqueueAll(next.map(_ -> (steps + 1))),
          visited ++ next,
          favoriteNumber,
          maxSteps,
        )
end count

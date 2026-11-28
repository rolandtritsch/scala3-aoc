package aoc2016.day17

import java.security.MessageDigest

import scala.annotation.tailrec
import scala.collection.immutable.Queue

/** Breadth-first search over paths, so the first path that reaches the vault is a shortest one.
  *
  * Each node is a distinct path (the doors depend on the whole path), so no visited set is needed:
  * breadth-first order reaches the vault at the fewest possible steps.
  */
private[day17] def shortestPath(passcode: Passcode): Option[Seq[Move]] =
  val digest = MessageDigest.getInstance("MD5")
  search(Queue((Start, Seq.empty[Move])), digest, passcode)

@tailrec
private def search(
    queue: Queue[(Position, Seq[Move])],
    digest: MessageDigest,
    passcode: Passcode,
): Option[Seq[Move]] =
  // A FIFO queue keeps breadth-first order without repeated O(n) Seq appends.
  queue.dequeueOption match
    case None                                => None
    case Some(((position, path), remaining)) =>
      if position == Vault then Some(path)
      else
        val next = openDoors(digest, passcode, path).toSeq
          .map(move => (moveTo(position, move), path :+ move))
          .filter((target, _) => isInside(target))
        search(remaining.enqueueAll(next), digest, passcode)
end search

package aoc2016.day17

import java.security.MessageDigest

import scala.annotation.tailrec

/** The length of the longest path that reaches the vault, or `None` if the vault is unreachable. */
def part2(passcode: Passcode): Option[Int] = longestPath(passcode)

/** Depth-first search over every path, keeping the greatest length that reaches the vault.
  *
  * Each node is a distinct path because the open doors depend on the whole path, so the search
  * visits every possible route. Paths end the first time they reach the vault, so the vault is
  * never expanded and the maximum recorded length is the answer.
  */
private def longestPath(passcode: Passcode): Option[Int] =
  val digest = MessageDigest.getInstance("MD5")
  searchLongest(List((Start, Seq.empty[Move])), digest, passcode, None)

@tailrec
private def searchLongest(
    pending: List[(Position, Seq[Move])],
    digest: MessageDigest,
    passcode: Passcode,
    longest: Option[Int],
): Option[Int] = pending match
  case Nil                      => longest
  case (position, path) :: rest =>
    if position == Vault then
      val reached = path.length
      searchLongest(rest, digest, passcode, Some(longest.fold(reached)(math.max(_, reached))))
    else
      val next = openDoors(digest, passcode, path).toSeq
        .map(move => (moveTo(position, move), path :+ move)).filter((target, _) => isInside(target))
      searchLongest(next.toList ::: rest, digest, passcode, longest)
end searchLongest

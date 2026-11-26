package aoc2016.day04

def part1(input: Seq[Room]): Int = input.withFilter(isReal).map(_.sectorId).sum

private def isReal(room: Room): Boolean = room.checksum == expectedChecksum(room)

private def expectedChecksum(room: Room): String =
  val counts = room.encryptedName.flatMap(_.toSeq).groupMapReduce(identity)(_ => 1)(_ + _)
  counts.toSeq.sortBy { case (letter, count) => (-count, letter) }.take(5).map { case (letter, _) =>
    letter
  }.mkString

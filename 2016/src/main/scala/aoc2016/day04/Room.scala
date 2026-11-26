package aoc2016.day04

/** A room with a validated encrypted name, sector ID, and five-letter checksum. */
final case class Room(encryptedName: Seq[String], sectorId: Int, checksum: String)

def isReal(room: Room): Boolean = room.checksum == expectedChecksum(room)

private def expectedChecksum(room: Room): String =
  val counts = room.encryptedName.flatMap(_.toSeq).groupMapReduce(identity)(_ => 1)(_ + _)
  counts.toSeq.sortBy { case (letter, count) => (-count, letter) }.take(5).map { case (letter, _) =>
    letter
  }.mkString

package aoc2016.day04

/** A room with a validated encrypted name, sector ID, and five-letter checksum. */
final case class Room(encryptedName: Seq[String], sectorId: Int, checksum: String)

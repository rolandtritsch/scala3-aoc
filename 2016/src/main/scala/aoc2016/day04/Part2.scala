package aoc2016.day04

private val NorthPoleObjectStorage = "northpole object storage"

def part2(input: Seq[Room]): Option[Int] = input.collectFirst {
  case room if isReal(room) && decryptName(room) == NorthPoleObjectStorage => room.sectorId
}

def decryptName(room: Room): String = room.encryptedName
  .map(_.map(letter => rotate(letter, room.sectorId)).mkString).mkString(" ")

private def rotate(letter: Char, sectorId: Int): Char = ('a' + (letter - 'a' + sectorId % 26) % 26)
  .toChar

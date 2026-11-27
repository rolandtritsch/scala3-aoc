package aoc2016.day05

import scala.annotation.tailrec

def part2(doorId: DoorId): String =
  // Indexed updates retain the first character found for each position without rebuilding the password.
  val password = Array.fill[Option[Char]](PasswordLength)(None)

  @tailrec
  def decrypt(hashes: Iterator[Array[Byte]], remaining: Int): String =
    if remaining == 0 then password.iterator.flatten.mkString
    else
      val hash = hashes.next()
      val position = sixthHexDigit(hash).asDigit
      val nextRemaining =
        if position < PasswordLength && password(position).isEmpty then
          password(position) = Some(seventhHexDigit(hash))
          remaining - 1
        else remaining
      decrypt(hashes, nextRemaining)
    end if
  end decrypt

  decrypt(interestingHashes(doorId), PasswordLength)
end part2

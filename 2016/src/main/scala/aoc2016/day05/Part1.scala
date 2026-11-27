package aoc2016.day05

import java.nio.charset.StandardCharsets
import java.security.MessageDigest

private val PasswordLength = 8
private val HexDigits = "0123456789abcdef"

def part1(doorId: DoorId): String =
  val md5 = MessageDigest.getInstance("MD5")

  // Laziness stops the search as soon as all eight password characters are known.
  Iterator.from(0).map(index => md5.digest(s"$doorId$index".getBytes(StandardCharsets.UTF_8)))
    .withFilter(hasFiveZeroPrefix).map(hash => HexDigits.charAt(hash(2) & 0x0f))
    .take(PasswordLength).mkString
end part1

private def hasFiveZeroPrefix(hash: Array[Byte]): Boolean =
  // MD5 has 16 bytes; five leading hexadecimal zeroes occupy these first two and a half bytes.
  hash(0) == 0 && hash(1) == 0 && (hash(2) & 0xf0) == 0

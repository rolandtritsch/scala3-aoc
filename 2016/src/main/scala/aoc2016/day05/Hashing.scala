package aoc2016.day05

import java.nio.charset.StandardCharsets
import java.security.MessageDigest

private[day05] val PasswordLength = 8
private val HexDigits = "0123456789abcdef"

// Laziness lets each part stop hashing as soon as its password is complete.
private[day05] def interestingHashes(doorId: DoorId): Iterator[Array[Byte]] =
  val md5 = MessageDigest.getInstance("MD5")
  Iterator.from(0).map(index => md5.digest(s"$doorId$index".getBytes(StandardCharsets.UTF_8)))
    .filter(hasFiveZeroPrefix)

private[day05] def sixthHexDigit(hash: Array[Byte]): Char = HexDigits.charAt(hash(2) & 0x0f)

private[day05] def seventhHexDigit(hash: Array[Byte]): Char = HexDigits
  .charAt((hash(3) & 0xf0) >>> 4)

private def hasFiveZeroPrefix(hash: Array[Byte]): Boolean =
  // MD5 has 16 bytes; five leading hexadecimal zeroes occupy these first two and a half bytes.
  hash(0) == 0 && hash(1) == 0 && (hash(2) & 0xf0) == 0

package aoc2016.day17

import java.nio.charset.StandardCharsets
import java.security.MessageDigest

private val HexDigits = "0123456789abcdef"
private val OpenDoorCharacters = "bcdef"

/** The lowercase hexadecimal MD5 of the given input string.
  *
  * The digest is reused across calls because `digest` resets its state after each call.
  */
private[day17] def md5Hex(digest: MessageDigest, input: String): String = digest
  .digest(input.getBytes(StandardCharsets.UTF_8)).map { byte =>
    val value = byte & 0xff
    s"${HexDigits.charAt(value >>> 4)}${HexDigits.charAt(value & 0x0f)}"
  }.mkString

/** The first four hexadecimal characters of the hash of the passcode followed by the path.
  *
  * They encode, in order, whether the up, down, left, and right doors are open.
  */
private[day17] def firstFourHex(
    digest: MessageDigest,
    passcode: Passcode,
    path: Seq[Move],
): String = md5Hex(digest, s"${passcode.value}${pathString(path)}").take(4)

/** The moves whose doors are open, in the canonical U, D, L, R order. */
private[day17] def openDoors(
    digest: MessageDigest,
    passcode: Passcode,
    path: Seq[Move],
): Seq[Move] =
  val hash = firstFourHex(digest, passcode, path)
  MovesByHashIndex.zipWithIndex.collect:
    case (move, index) if OpenDoorCharacters.contains(hash(index)) => move
end openDoors

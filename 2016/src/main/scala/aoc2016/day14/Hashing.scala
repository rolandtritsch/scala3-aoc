package aoc2016.day14

import java.nio.charset.StandardCharsets
import java.security.MessageDigest

private val HexDigits = "0123456789abcdef"
private val Stretching = 2017

/** The lowercase hexadecimal MD5 of the given input string.
  *
  * The digest is reused across calls because `digest` resets its state after each call.
  */
private[day14] def md5Hex(digest: MessageDigest, input: String): String = digest
  .digest(input.getBytes(StandardCharsets.UTF_8)).map { byte =>
    val value = byte & 0xff
    s"${HexDigits.charAt(value >>> 4)}${HexDigits.charAt(value & 0x0f)}"
  }.mkString

/** The seed string for a key index: the salt followed by the decimal index. */
private[day14] def seed(salt: Salt, index: Int): String = s"${salt.value}$index"

/** The key-stretched hash: the seed hashed `Stretching` times in total (2016 additional). */
private[day14] def stretchedHex(digest: MessageDigest, input: String): String = (1 until Stretching)
  .foldLeft(md5Hex(digest, input))((hash, _) => md5Hex(digest, hash))

/** The first character that appears three times in a row, if any. */
private[day14] def firstTriplet(hash: String): Option[Char] = (0 to hash.length - 3).iterator
  .find(index => hash(index) == hash(index + 1) && hash(index) == hash(index + 2)).map(hash)

/** Whether the hash contains the given character five times in a row. */
private[day14] def hasQuint(hash: String, character: Char): Boolean = hash.sliding(5)
  .exists(_.forall(_ == character))

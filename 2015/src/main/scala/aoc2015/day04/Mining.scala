package aoc2015.day04

import java.nio.charset.StandardCharsets
import java.security.MessageDigest

import aoc2015.util.AocError

/** A fresh digest is local to each call; no mutable resource escapes. */
def md5Hex(s: String): String =
  val digest = MessageDigest.getInstance("MD5").digest(s.getBytes(StandardCharsets.UTF_8))
  digest.map(b => f"${b & 0xff}%02x").mkString

/** Search positive Int candidates in order, failing explicitly rather than wrapping at
  * Int.MaxValue.
  */
private[day04] def findLowest(secretKey: SecretKey, prefix: String): Either[AocError, Int] =
  @scala.annotation.tailrec
  def search(n: Int): Either[AocError, Int] =
    if md5Hex(secretKeyValue(secretKey) + n).startsWith(prefix) then Right(n)
    else if n == Int.MaxValue then
      Left(AocError.ValidationError("no matching positive Int candidate"))
    else search(n + 1)

  search(1)

end findLowest

package aoc2015

import com.typesafe.scalalogging.Logger

import java.nio.charset.StandardCharsets
import java.security.MessageDigest

/** Day04 - The Ideal Stocking Stuffer
  *
  * Santa needs to find MD5 hashes whose hexadecimal representation starts with a run of zeroes. The
  * input to the hash is a secret key (the puzzle input) immediately followed by a decimal number
  * with no leading zeroes. The numbers are tried in increasing order `1, 2, 3, ...`, and the first
  * one whose hash has the required prefix is the answer.
  *
  * part1: the lowest positive integer `n` such that the MD5 hex digest of `key + n` starts with
  * five zeroes (`00000`).
  *
  * part2: the lowest positive integer `n` such that the MD5 hex digest of `key + n` starts with six
  * zeroes (`000000`).
  */
object Day04:

  val logger: Logger = Logger(this.getClass.getName)

  /** @return
    *   the secret key read from the given resource file, with surrounding whitespace removed.
    */
  def readFile(filename: String): String =
    import scala.io.Source

    require(filename.nonEmpty, "filename.nonEmpty")
    logger.debug(s"filename: ${filename}")

    val source = Source.fromResource(filename)
    try source.mkString.trim
    finally source.close()
    end try
  end readFile

  /** @return the lowercase hexadecimal representation of the MD5 digest of `s`. */
  def md5Hex(s: String): String =
    val digest = MessageDigest.getInstance("MD5").digest(s.getBytes(StandardCharsets.UTF_8))
    digest.map(b => f"${b & 0xff}%02x").mkString
  end md5Hex

  /** @return
    *   the lowest positive integer `n` such that the MD5 hex digest of `secretKey + n` starts with
    *   `prefix`. It scans candidates `1, 2, 3, ...` in increasing order (positive, no leading
    *   zeroes), computing one MD5 digest of a short string per candidate and returning the first
    *   match, which is therefore also the lowest match. The scan is tail-recursive so it does not
    *   grow the call stack regardless of how far the answer lies. Runs in O(a * len) time, where
    *   `a` is the answer and `len` is the length of the secret key, using O(1) extra space.
    */
  private def findLowest(secretKey: String, prefix: String): Int =
    @scala.annotation.tailrec
    def search(n: Int): Int = if md5Hex(secretKey + n).startsWith(prefix) then n else search(n + 1)

    search(1)
  end findLowest

  /** @return
    *   the lowest positive integer `n` such that the MD5 hex digest of `secretKey + n` starts with
    *   five zeroes (`00000`).
    */
  def part1(secretKey: String): Int =
    require(secretKey.nonEmpty, "secretKey.nonEmpty")
    logger.debug(s"secretKey: ${secretKey}")

    findLowest(secretKey, "00000")
  end part1

  /** @return
    *   the lowest positive integer `n` such that the MD5 hex digest of `secretKey + n` starts with
    *   six zeroes (`000000`).
    */
  def part2(secretKey: String): Int =
    require(secretKey.nonEmpty, "secretKey.nonEmpty")
    logger.debug(s"secretKey: ${secretKey}")

    findLowest(secretKey, "000000")
  end part2

end Day04

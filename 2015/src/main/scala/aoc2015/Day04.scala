package aoc2015

import java.nio.charset.StandardCharsets
import java.security.MessageDigest

import zio.*

/** Day04 - The Ideal Stocking Stuffer. */
object Day04:

  opaque type SecretKey = String

  /** The key is text, but its type guarantees it is one non-empty token. */
  def parse(filename: String, input: String): Either[AocError, SecretKey] =
    val key = input.trim
    if key.nonEmpty && !key.exists(_.isWhitespace) then Right(key)
    else
      Left(
        AocError
          .ParseError(filename, "expected one non-empty secret key without internal whitespace")
      )
    end if

  end parse

  def readFile(filename: String): ZIO[Any, AocError, SecretKey] =
    import scala.io.Source

    ZIO.acquireReleaseWith(
      ZIO.attempt(Source.fromResource(filename)).mapError(AocError.FileReadError(filename, _))
    )(source => ZIO.succeed(source.close())) { source =>
      for
        _ <- ZIO.logDebug(s"filename: $filename")
        input <- ZIO.attempt(source.mkString).mapError(AocError.FileReadError(filename, _))
        key <- ZIO.fromEither(parse(filename, input))
        _ <- ZIO.logDebug(s"parsed: $key")
      yield key
    }

  end readFile

  /** A fresh digest is local to each call; no mutable resource escapes. */
  def md5Hex(s: String): String =
    val digest = MessageDigest.getInstance("MD5").digest(s.getBytes(StandardCharsets.UTF_8))
    digest.map(b => f"${b & 0xff}%02x").mkString

  /** Search positive Int candidates in order, failing explicitly rather than wrapping at
    * Int.MaxValue.
    */
  private def findLowest(secretKey: SecretKey, prefix: String): Either[AocError, Int] =
    @scala.annotation.tailrec
    def search(n: Int): Either[AocError, Int] =
      if md5Hex(secretKey + n).startsWith(prefix) then Right(n)
      else if n == Int.MaxValue then
        Left(AocError.ValidationError("no matching positive Int candidate"))
      else search(n + 1)

    search(1)

  end findLowest

  def part1(secretKey: SecretKey): Either[AocError, Int] = findLowest(secretKey, "00000")

  def part2(secretKey: SecretKey): Either[AocError, Int] = findLowest(secretKey, "000000")

end Day04

package aoc2016.day14

import java.security.MessageDigest

import scala.annotation.tailrec

private val KeyCount = 64
private val Window = 1000

/** The index of the 64th one-time pad key for the given salt. */
def part1(salt: Salt): Int =
  val digest = MessageDigest.getInstance("MD5")
  search(salt, digest, 0, 0, Vector.empty)

@tailrec
private def search(
    salt: Salt,
    digest: MessageDigest,
    index: Int,
    found: Int,
    cache: Vector[String],
): Int =
  // Keep a thousand hashes of lookahead so each candidate can be checked without recomputation.
  val extended = extend(cache, salt, digest, index + Window)
  val nextFound = firstTriplet(extended(index)) match
    case Some(character) if hasQuintInWindow(extended, index, character) => found + 1
    case _                                                               => found
  if nextFound == KeyCount then index else search(salt, digest, index + 1, nextFound, extended)
end search

@tailrec
private def extend(
    cache: Vector[String],
    salt: Salt,
    digest: MessageDigest,
    upto: Int,
): Vector[String] =
  if cache.length > upto then cache
  else extend(cache :+ md5Hex(digest, salt, cache.length), salt, digest, upto)
end extend

private def hasQuintInWindow(cache: Vector[String], index: Int, character: Char): Boolean =
  (index + 1 to index + Window).exists(offset => hasQuint(cache(offset), character))

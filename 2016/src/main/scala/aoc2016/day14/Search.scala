package aoc2016.day14

import java.security.MessageDigest

import scala.annotation.tailrec

private val KeyCount = 64
private val Window = 1000

/** The index of the 64th one-time pad key, where `hash` turns a seed into its hex digest.
  *
  * A thousand hashes of lookahead are cached so each candidate's quint search needs no
  * recomputation. `found` counts keys confirmed so far and `index` is the candidate position.
  */
@tailrec
def searchKey(
    salt: Salt,
    digest: MessageDigest,
    index: Int,
    found: Int,
    cache: Vector[String],
    hash: String => String,
): Int =
  val extended = extend(cache, salt, digest, index + Window, hash)
  val nextFound = firstTriplet(extended(index)) match
    case Some(character) if hasQuintInWindow(extended, index, character) => found + 1
    case _                                                               => found
  if nextFound == KeyCount then index
  else searchKey(salt, digest, index + 1, nextFound, extended, hash)
end searchKey

@tailrec
private def extend(
    cache: Vector[String],
    salt: Salt,
    digest: MessageDigest,
    upto: Int,
    hash: String => String,
): Vector[String] =
  if cache.length > upto then cache
  else extend(cache :+ hash(seed(salt, cache.length)), salt, digest, upto, hash)
end extend

private def hasQuintInWindow(cache: Vector[String], index: Int, character: Char): Boolean =
  (index + 1 to index + Window).exists(offset => hasQuint(cache(offset), character))

package aoc2016.day14

import java.security.MessageDigest

/** The index of the 64th one-time pad key for the given salt. */
def part1(salt: Salt): Int =
  val digest = MessageDigest.getInstance("MD5")
  searchKey(salt, digest, 0, 0, Vector.empty, input => md5Hex(digest, input))

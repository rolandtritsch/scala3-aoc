package aoc2016.day14

import java.security.MessageDigest

/** The index of the 64th one-time pad key when every hash is key-stretched 2016 times. */
def part2(salt: Salt): Int =
  val digest = MessageDigest.getInstance("MD5")
  searchKey(salt, digest, 0, 0, Vector.empty, input => stretchedHex(digest, input))

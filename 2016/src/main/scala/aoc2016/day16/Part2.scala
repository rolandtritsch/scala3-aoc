package aoc2016.day16

import scala.annotation.tailrec

/** The number of checksum digits required for part two. */
private val Part2DiskLength = 35651584

/** The part-two answer: the checksum of the dragon curve truncated to `Part2DiskLength` bits. */
def part2(input: Input): String = checksumFor(input.bits, Part2DiskLength)

/** Compute the checksum of the dragon curve grown from `initial` and truncated to `length` digits.
  */
def checksumFor(initial: Seq[Bit], length: Int): String =
  checksumDigits(grow(initial.map(bitToByte).toArray, length), length)

/** Grow the dragon curve in a flat byte array until it holds at least `length` digits.
  *
  * A local mutable `Array` is the O(1)-space accumulator for the curve: each expansion doubles the
  * previous digits, so the total copying work is linear in the final length rather than quadratic
  * in the number of expansions.
  */
@tailrec
private def grow(bits: Array[Byte], length: Int): Array[Byte] =
  if bits.length >= length then bits else grow(expand(bits), length)

/** One dragon-curve expansion: the current digits, a 0, then the current digits reversed and
  * flipped.
  */
private def expand(bits: Array[Byte]): Array[Byte] =
  val grown = new Array[Byte](2 * bits.length + 1)
  Array.copy(bits, 0, grown, 0, bits.length)
  grown(bits.length) = 0
  for i <- 0 until bits.length do grown(bits.length + 1 + i) = flipByte(bits(bits.length - 1 - i))
  grown
end expand

/** The opposite digit, used when building the reversed half of the curve. */
private def flipByte(bit: Byte): Byte = if bit == 0 then 1 else 0

/** Encode a parsed digit as its byte form for compact curve storage. */
private def bitToByte(bit: Bit): Byte = bit match
  case Bit.Zero => 0
  case Bit.One  => 1

/** Repeatedly fold equal adjacent pairs to 1 and differing pairs to 0 until an odd count remains.
  */
@tailrec
private def checksumDigits(bits: Array[Byte], length: Int): String =
  if length % 2 == 1 then renderBytes(bits, length)
  else checksumDigits(reducePairs(bits, length), length / 2)

/** Replace each adjacent pair with 1 when the two digits match and 0 otherwise. */
private def reducePairs(bits: Array[Byte], length: Int): Array[Byte] =
  val reduced = new Array[Byte](length / 2)
  for i <- 0 until length / 2 do reduced(i) = if bits(2 * i) == bits(2 * i + 1) then 1 else 0
  reduced

/** Render the first `length` digits as their '0'/'1' characters. */
private def renderBytes(bits: Array[Byte], length: Int): String = (0 until length)
  .map((i) => if bits(i) == 1 then '1' else '0').mkString

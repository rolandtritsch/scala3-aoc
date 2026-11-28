package aoc2016.day16

import scala.annotation.tailrec

/** The number of checksum digits required for part one. */
private val DiskLength = 272

/** The part-one answer: the checksum of the dragon curve truncated to `DiskLength` bits. */
def part1(input: Input): String = checksum(dragon(input.bits, DiskLength).take(DiskLength))

/** Grow the dragon curve by appending a 0 and the flipped reverse until it reaches `length` digits.
  */
@tailrec
def dragon(bits: Seq[Bit], length: Int): Seq[Bit] =
  if bits.length >= length then bits else dragon(step(bits), length)

/** One dragon-curve expansion: the current bits, a 0, then the current bits reversed and flipped.
  */
def step(bits: Seq[Bit]): Seq[Bit] = bits ++ (Bit.Zero +: bits.reverse.map(_.flip))

/** Repeatedly fold equal adjacent pairs to 1 and differing pairs to 0 until an odd count remains.
  */
@tailrec
def checksum(bits: Seq[Bit]): String =
  if bits.length % 2 == 1 then render(bits) else checksum(reduce(bits))

/** Replace each adjacent pair with 1 when the two bits match and 0 otherwise. */
def reduce(bits: Seq[Bit]): Seq[Bit] = bits.grouped(2).map {
  case Seq(a, b) => if a == b then Bit.One else Bit.Zero
  case _         => Bit.Zero
}.toSeq

/** Render bits as their '0'/'1' characters. */
def render(bits: Seq[Bit]): String = bits.map {
  case Bit.Zero => "0"
  case Bit.One  => "1"
}.mkString

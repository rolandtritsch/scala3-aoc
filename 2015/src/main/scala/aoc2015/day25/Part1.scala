package aoc2015.day25

import scala.annotation.tailrec

private val FirstCode = 20151125L
private val Multiplier = 252533L
private val Modulus = 33554393L

/** Return the code at the requested position. */
def part1(coordinate: Coordinate): Long = FirstCode *
  modularPower(Multiplier, codeIndex(coordinate) - 1) % Modulus

/** Diagonal `row + column` contains positions whose column increases as the row decreases. */
private[day25] def codeIndex(coordinate: Coordinate): BigInt =
  val diagonal = BigInt(coordinate.row) + BigInt(coordinate.column)
  (diagonal - 2) * (diagonal - 1) / 2 + BigInt(coordinate.column)

@tailrec
private def modularPower(base: Long, exponent: BigInt, result: Long = 1L): Long =
  if exponent == 0 then result
  else
    val nextResult = if exponent % 2 == 1 then result * base % Modulus else result
    modularPower(base * base % Modulus, exponent / 2, nextResult)

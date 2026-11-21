package aoc2015

import aoc2015.Day04.*

class Day04Test extends munit.ScalaCheckSuite:

  test("Day04 - md5Hex - independent RFC 1321 vectors"):
    assertEquals(md5Hex(""), "d41d8cd98f00b204e9800998ecf8427e")
    assertEquals(md5Hex("abc"), "900150983cd24fb0d6963f7d28e17f72")

  test("Day04 - part1 - official samples"):
    assertEquals(part1("abcdef"), 609043)
    assertEquals(part1("pqrstuv"), 1048970)

  test("Day04 - part1 - empty key is rejected"):
    intercept[IllegalArgumentException](part1(""))

  test("Day04 - part1 - found hash actually has the required prefix"):
    assertEquals(part1("bgvyzdsv"), 254575)

  test("Day04 - part1 - real input"):
    val input = readFile("inputs/Day04.txt")
    assertEquals(part1(input), 254575)

  test("Day04 - part2 - empty key is rejected"):
    intercept[IllegalArgumentException](part2(""))

  test("Day04 - part2 - found hash actually has the required prefix"):
    val n = part2("bgvyzdsv")
    assert(md5Hex("bgvyzdsv" + n).startsWith("000000"), s"hash of bgvyzdsv$n lacks six zeroes")
    assert(!md5Hex("bgvyzdsv" + (n - 1)).startsWith("000000"), s"n-1 already matches")

  test("Day04 - part2 - real input"):
    val input = readFile("inputs/Day04.txt")
    assertEquals(part2(input), 1038736)

end Day04Test

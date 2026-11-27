package aoc2016.day07

/** Count addresses with an ABBA outside brackets and no ABBA inside brackets. */
def part1(addresses: Seq[IpAddress]): Int = addresses.count(supportsTls)

private def supportsTls(address: IpAddress): Boolean = !address.hypernets.exists(hasAbba) &&
  address.supernets.exists(hasAbba)

private def hasAbba(text: String): Boolean = text.sliding(4).exists { window =>
  window.length == 4 && window(0) == window(3) && window(1) == window(2) && window(0) != window(1)
}

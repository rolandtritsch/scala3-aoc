package aoc2016.day07

/** Count addresses with a supernet ABA and its corresponding hypernet BAB. */
def part2(addresses: Seq[IpAddress]): Int = addresses.count(supportsSsl)

private def supportsSsl(address: IpAddress): Boolean = address.supernets.exists { supernet =>
  supernet.sliding(3).exists { aba =>
    isAba(aba) && address.hypernets.exists(_.contains(s"${aba(1)}${aba(0)}${aba(1)}"))
  }
}

private def isAba(window: String): Boolean = window.length == 3 && window(0) == window(2) &&
  window(0) != window(1)

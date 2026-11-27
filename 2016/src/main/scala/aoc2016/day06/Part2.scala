package aoc2016.day06

/** Recover the original message by taking the least frequent character in each column. */
def part2(recording: Recording): String =
  val signals = recording.signals
  signals.headOption match
    case None        => ""
    case Some(first) => first.indices.map { position =>
        val frequencies = signals.map(_.apply(position)).groupMapReduce(identity)(_ => 1)(_ + _)
        frequencies.minBy(_._2)._1
      }.mkString
end part2

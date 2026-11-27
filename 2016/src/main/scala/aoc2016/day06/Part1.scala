package aoc2016.day06

/** Recover the message by taking the most frequent character in each column. */
def part1(recording: Recording): String =
  val signals = recording.signals
  signals.headOption match
    case None        => ""
    case Some(first) => first.indices.map { position =>
        val frequencies = signals.map(_.apply(position)).groupMapReduce(identity)(_ => 1)(_ + _)
        frequencies.maxBy(_._2)._1
      }.mkString
end part1

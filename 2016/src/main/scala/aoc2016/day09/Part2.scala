package aoc2016.day09

def part2(input: CompressedFile): BigInt = input.chunks.foldLeft(BigInt(0)) {
  case (total, Chunk.Literal(text))              => total + text.length
  case (total, Chunk.Repeat(_, times, contents)) => total + part2(contents) * times
}

package aoc2016.day09

def part1(input: CompressedFile): BigInt = input.chunks.foldLeft(BigInt(0)) {
  case (total, Chunk.Literal(text))          => total + text.length
  case (total, Chunk.Repeat(data, times, _)) => total + BigInt(data.length) * times
}

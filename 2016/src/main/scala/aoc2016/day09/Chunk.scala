package aoc2016.day09

enum Chunk:
  case Literal(text: String)
  case Repeat(data: String, times: Int)
end Chunk

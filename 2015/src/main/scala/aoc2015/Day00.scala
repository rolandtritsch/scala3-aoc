package aoc2015

import zio.*

/** Day00 - runnable Fibonacci example. The first and last non-negative indices select each part. */
object Day00:

  opaque type Index = Int
  final case class Input(first: Index, last: Index)

  def parseIndex(filename: String, line: String, lineNumber: Int = 1): Either[AocError, Index] =
    line.trim.toIntOption.filter(_ >= 0).toRight(
      AocError.ParseError(filename, s"line $lineNumber: expected a non-negative Int index: $line")
    )

  /** At least one index is required; a single index selects the same Fibonacci number for both
    * parts.
    */
  def parse(filename: String, input: String): Either[AocError, Input] = input.linesIterator
    .zipWithIndex
    .foldLeft[Either[AocError, Seq[Index]]](Right(Seq.empty)) { case (result, (line, index)) =>
      for
        indices <- result
        parsed <- parseIndex(filename, line, index + 1)
      yield parsed +: indices
    }.flatMap { reversed =>
      (reversed.lastOption, reversed.headOption) match
        case (Some(first), Some(last)) => Right(Input(first, last))
        case _ => Left(AocError.ParseError(filename, "expected at least one Fibonacci index"))
    }

  def readFile(filename: String): ZIO[Any, AocError, Input] =
    import scala.io.Source

    ZIO.acquireReleaseWith(
      ZIO.attempt(Source.fromResource(filename)).mapError(AocError.FileReadError(filename, _))
    )(source => ZIO.succeed(source.close())) { source =>
      for
        _ <- ZIO.logDebug(s"filename: $filename")
        input <- ZIO.attempt(source.mkString).mapError(AocError.FileReadError(filename, _))
        _ <- ZIO.foreachDiscard(input.linesIterator.zipWithIndex.toSeq) { (line, index) =>
          ZIO.logDebug(s"line ${index + 1}: $line")
        }
        parsed <- ZIO.fromEither(parse(filename, input))
        _ <- ZIO.logDebug(s"parsed: $parsed")
      yield parsed
    }

  end readFile

  /** The Index type guarantees a non-negative iteration count. BigInt keeps the result exact. */
  def fib(n: Index): BigInt =
    @scala.annotation.tailrec
    def go(i: Int, a: BigInt, b: BigInt): BigInt = if i >= n then a else go(i + 1, b, a + b)

    go(0, 0, 1)

  end fib

  def part1(input: Input): BigInt = fib(input.first)

  def part2(input: Input): BigInt = fib(input.last)

end Day00

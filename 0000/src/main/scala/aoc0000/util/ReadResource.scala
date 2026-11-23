package aoc0000.util

import zio.*

/** Read and close a classpath resource, then validate its entire contents before returning a value.
  */
def readResource[A](filename: String)(
    parse: (String, String) => Either[AocError, A]
): ZIO[Any, AocError, A] =
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

end readResource

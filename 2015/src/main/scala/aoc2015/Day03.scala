package aoc2015

import zio.*

/** Day03 - Perfectly Spherical Houses in a Vacuum. */
object Day03:

  enum Direction:
    case North, South, East, West

  final case class Position(x: Int, y: Int)

  /** Whitespace separates instructions and never consumes a mover's turn. */
  def parse(filename: String, input: String): Either[AocError, Seq[Direction]] = input.zipWithIndex
    .foldLeft[Either[AocError, Seq[Direction]]](Right(Seq.empty)) {
      case (result, (character, index)) => result.flatMap { directions =>
          character match
            case '^'                 => Right(Direction.North +: directions)
            case 'v'                 => Right(Direction.South +: directions)
            case '>'                 => Right(Direction.East +: directions)
            case '<'                 => Right(Direction.West +: directions)
            case c if c.isWhitespace => Right(directions)
            case c                   => Left(
                AocError.ParseError(filename, s"unexpected character '$c' at position ${index + 1}")
              )
        }
    }.map(_.reverse)

  def readFile(filename: String): ZIO[Any, AocError, Seq[Direction]] =
    import scala.io.Source

    ZIO.acquireReleaseWith(
      ZIO.attempt(Source.fromResource(filename)).mapError(AocError.FileReadError(filename, _))
    )(source => ZIO.succeed(source.close())) { source =>
      for
        _ <- ZIO.logDebug(s"filename: $filename")
        input <- ZIO.attempt(source.mkString).mapError(AocError.FileReadError(filename, _))
        directions <- ZIO.fromEither(parse(filename, input))
        _ <- ZIO.logDebug(s"parsed: $directions")
      yield directions
    }

  end readFile

  private def step(position: Position, direction: Direction): Position = direction match
    case Direction.North => position.copy(y = position.y + 1)
    case Direction.South => position.copy(y = position.y - 1)
    case Direction.East  => position.copy(x = position.x + 1)
    case Direction.West  => position.copy(x = position.x - 1)

  private val origin: Position = Position(0, 0)

  /** A Set deduplicates houses and supports efficient membership and union. */
  private def visited(directions: Seq[Direction]): Set[Position] = directions
    .foldLeft((Set(origin), origin)) { case ((houses, position), direction) =>
      val next = step(position, direction)
      (houses + next, next)
    }._1

  def part1(directions: Seq[Direction]): Int = visited(directions).size

  /** Even instruction indices belong to Santa; odd indices belong to Robo-Santa. */
  def part2(directions: Seq[Direction]): Int =
    val (santa, robo) = directions.zipWithIndex.partitionMap { (direction, index) =>
      if index % 2 == 0 then Left(direction) else Right(direction)
    }
    (visited(santa) union visited(robo)).size

  end part2

end Day03

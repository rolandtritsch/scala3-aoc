package aoc2015

import zio.*

/** Day01 - Not Quite Lisp
  *
  * Santa starts on floor 0 and follows a sequence of parentheses, one at a time. An opening
  * parenthesis `(` means he goes up one floor; a closing parenthesis `)` means he goes down one
  * floor.
  *
  * part1: the final floor reached after following every instruction. part2: the 1-based position of
  * the first instruction that sends Santa into the basement (floor -1), or -1 if he never enters
  * the basement.
  */
object Day01:

  enum Move:
    case Up, Down

  /** Parse parentheses into moves, ignoring whitespace and rejecting other characters. */
  def parse(filename: String, input: String): Either[AocError, Seq[Move]] = input.zipWithIndex
    .foldLeft[Either[AocError, Seq[Move]]](Right(Seq.empty)) { case (result, (character, index)) =>
      result.flatMap { moves =>
        character match
          case '('                 => Right(Move.Up +: moves)
          case ')'                 => Right(Move.Down +: moves)
          case c if c.isWhitespace => Right(moves)
          case c                   => Left(
              AocError.ParseError(filename, s"unexpected character '$c' at position ${index + 1}")
            )
      }
    }.map(_.reverse)
  end parse

  /** @return
    *   the moves parsed from the given resource file, or an [[AocError]] if the resource is
    *   unreadable or contains an invalid instruction.
    */
  def readFile(filename: String): ZIO[Any, AocError, Seq[Move]] =
    import scala.io.Source

    ZIO.acquireReleaseWith(
      ZIO.attempt(Source.fromResource(filename)).mapError(AocError.FileReadError(filename, _))
    )(source => ZIO.succeed(source.close())) { source =>
      for
        _ <- ZIO.logDebug(s"filename: $filename")
        input <- ZIO.attempt(source.mkString).mapError(AocError.FileReadError(filename, _))
        moves <- ZIO.fromEither(parse(filename, input))
        _ <- ZIO.logDebug(s"parsed: $moves")
      yield moves
    }
  end readFile

  /** @return the floor reached after applying one move. */
  private def step(floor: Int, move: Move): Int = move match
    case Move.Up   => floor + 1
    case Move.Down => floor - 1
  end step

  /** @return
    *   the running floor after each instruction, in order, with the starting floor 0 as the first
    *   element (before any instruction is applied). LazyList lets part2 stop at the first basement
    *   visit. Both parts share this construction, but each call performs its own scan.
    */
  private def floors(instructions: Seq[Move]): LazyList[Int] = instructions.to(LazyList)
    .scanLeft(0)(step)

  /** @return the final floor after following every instruction. */
  def part1(instructions: Seq[Move]): Int = floors(instructions).last

  /** @return
    *   the 1-based position of the first instruction that sends Santa into the basement (floor -1),
    *   or -1 if he never enters the basement. Since [[floors]] includes the starting floor at index
    *   0, each subsequent index equals the number of instructions applied. `LazyList.indexWhere`
    *   returns -1 when there is no match.
    */
  def part2(instructions: Seq[Move]): Int = floors(instructions).indexWhere(_ == -1)

end Day01

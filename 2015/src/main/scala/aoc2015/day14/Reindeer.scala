package aoc2015.day14

import aoc2015.util.AocError

/** A reindeer's race profile: how fast it flies and how long its fly/rest cycle lasts.
  *
  * A reindeer alternates between flying flat-out for `flySeconds` and resting for `restSeconds`,
  * repeating that cycle for the whole race.
  *
  * @param name
  *   the reindeer's name.
  * @param speed
  *   its top speed, in kilometres per second, while flying.
  * @param flySeconds
  *   the number of seconds it flies before it must rest.
  * @param restSeconds
  *   the number of seconds it rests before flying again.
  */
final case class Reindeer private (name: String, speed: Int, flySeconds: Int, restSeconds: Int)

object Reindeer:

  private val Line =
    """(.+) can fly (\d+) km/s for (\d+) seconds, but then must rest for (\d+) seconds\.""".r

  /** Parse one `Name can fly S km/s for F seconds, but then must rest for R seconds.` line.
    *
    * The speed, fly time, and rest time are non-negative integers with no upper bound other than
    * the integer range; each must be positive for the reindeer to be raceable.
    *
    * @param filename
    *   the resource name, used for error messages.
    * @param line
    *   the raw input line.
    * @param lineNumber
    *   the one-based line number, used for error messages.
    * @return
    *   the parsed reindeer, or a parse error.
    */
  def parse(filename: String, line: String, lineNumber: Int = 1): Either[AocError, Reindeer] =
    def invalid(reason: String): Left[AocError, Nothing] =
      Left(AocError.ParseError(filename, s"line $lineNumber: $reason: $line"))

    line.trim match
      case Line(name, speedText, flyText, restText) =>
        val speed = speedText.toIntOption
        val flySeconds = flyText.toIntOption
        val restSeconds = restText.toIntOption
        (speed, flySeconds, restSeconds) match
          case (Some(s), Some(f), Some(r)) if s > 0 && f > 0 && r > 0 =>
            Right(Reindeer(name, s, f, r))
          case _ => invalid("speed, fly seconds, and rest seconds must be positive integers")
      case _ =>
        invalid("expected \"Name can fly N km/s for M seconds, but then must rest for K seconds.\"")
    end match
  end parse

end Reindeer

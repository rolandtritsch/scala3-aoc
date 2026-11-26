package aoc2016.day03

import aoc2016.util.AocError

def part2(input: Seq[Triangle]): Either[AocError, Int] = input.grouped(3)
  .foldLeft[Either[AocError, Int]](Right(0)) {
    case (Right(count), Seq(first, second, third)) =>
      val triangles = Seq(
        Triangle(first.first, second.first, third.first),
        Triangle(first.second, second.second, third.second),
        Triangle(first.third, second.third, third.third),
      )
      Right(count + triangles.count(isPossible))
    case (Right(_), incomplete) => Left(AocError.ValidationError(
        s"Day03 column groups require three rows; the final group has ${incomplete.size}"
      ))
    case (error @ Left(_), _) => error
  }
end part2

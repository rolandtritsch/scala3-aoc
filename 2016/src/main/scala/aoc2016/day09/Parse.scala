package aoc2016.day09

import scala.annotation.tailrec

import aoc2016.util.AocError

private val Marker = """\((\d+)x(\d+)\)""".r

def parse(filename: String, raw: String): Either[AocError, CompressedFile] =
  val input = raw.filterNot(_.isWhitespace)

  @tailrec
  def loop(index: Int, reversed: List[Chunk]): Either[AocError, CompressedFile] =
    if index >= input.length then Right(CompressedFile(reversed.reverse))
    else if input(index) == '(' then
      val matcher = Marker.pattern.matcher(input).region(index, input.length)
      if !matcher.lookingAt() then
        Left(
          AocError
            .ParseError(filename, s"malformed marker at non-whitespace character ${index + 1}")
        )
      else
        (matcher.group(1).toIntOption, matcher.group(2).toIntOption) match
          case (Some(length), Some(times)) =>
            val dataStart = matcher.end()
            val dataEnd = dataStart.toLong + length
            if dataEnd > input.length then
              Left(AocError.ParseError(
                filename,
                s"marker at non-whitespace character ${index + 1} needs $length data characters",
              ))
            else
              loop(
                dataEnd.toInt,
                Chunk.Repeat(input.substring(dataStart, dataEnd.toInt), times) :: reversed,
              )
          case _ => Left(AocError.ParseError(
              filename,
              s"marker value out of range at non-whitespace character ${index + 1}",
            ))
    else
      val nextMarker = input.indexOf('(', index)
      val end = if nextMarker < 0 then input.length else nextMarker
      loop(end, Chunk.Literal(input.substring(index, end)) :: reversed)
  end loop

  loop(0, Nil)
end parse

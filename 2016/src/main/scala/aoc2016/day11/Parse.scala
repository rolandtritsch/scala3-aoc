package aoc2016.day11

import aoc2016.util.AocError

private val ItemPattern = """a ([a-z]+) generator|a ([a-z]+)-compatible microchip""".r
private val FloorNames = Seq("first", "second", "third", "fourth")
private val Separators = Seq(", and ", " and ", ", ")

def parse(filename: String, input: String): Either[AocError, Facility] =
  val lines = input.linesIterator.toSeq
  if lines.size != FloorCount then
    Left(AocError.ParseError(filename, s"expected exactly $FloorCount floor descriptions"))
  else
    lines.zipWithIndex.foldLeft[Either[AocError, Seq[Seq[Item]]]](Right(Seq.empty)) {
      case (result, (line, index)) =>
        for
          floors <- result
          floor <- parseFloor(filename, line, index + 1, index)
        yield floors :+ floor
    }.flatMap(floors => validatePairs(filename, floors))
end parse

private def parseFloor(
    filename: String,
    line: String,
    lineNumber: Int,
    floor: Int,
): Either[AocError, Seq[Item]] =
  val prefix = s"The ${FloorNames(floor)} floor contains "
  if !line.startsWith(prefix) || !line.endsWith(".") then
    Left(parseError(filename, lineNumber, s"expected the ${FloorNames(floor)} floor description"))
  else
    val contents = line.substring(prefix.length, line.length - 1)
    if contents == "nothing relevant" then Right(Seq.empty)
    else parseItems(filename, lineNumber, contents)
end parseFloor

private def parseItems(
    filename: String,
    lineNumber: Int,
    contents: String,
): Either[AocError, Seq[Item]] =
  for
    first <- parseItemAt(filename, lineNumber, contents, 0)
    items <-
      parseRemainingItems(filename, lineNumber, contents, first.nextPosition, Seq(first.item))
  yield items
end parseItems

private def parseRemainingItems(
    filename: String,
    lineNumber: Int,
    contents: String,
    position: Int,
    items: Seq[Item],
): Either[AocError, Seq[Item]] =
  if position == contents.length then Right(items)
  else
    Separators.find(contents.startsWith(_, position)) match
      case None => Left(parseError(filename, lineNumber, "expected an item-list separator"))
      case Some(separator) =>
        parseItemAt(filename, lineNumber, contents, position + separator.length).fold(
          error => Left(error),
          next =>
            val updated = items :+ next.item
            if separator.contains("and") then
              if next.nextPosition == contents.length then Right(updated)
              else Left(parseError(filename, lineNumber, "'and' may only introduce the final item"))
            else parseRemainingItems(filename, lineNumber, contents, next.nextPosition, updated),
        )
end parseRemainingItems

private def parseItemAt(
    filename: String,
    lineNumber: Int,
    contents: String,
    position: Int,
): Either[AocError, ParsedItem] = ItemPattern.findPrefixMatchOf(contents.drop(position)) match
  case None => Left(parseError(
      filename,
      lineNumber,
      s"expected a generator or compatible microchip at character ${position + 1}",
    ))
  case Some(matched) =>
    val item = (Option(matched.group(1)), Option(matched.group(2))) match
      case (Some(generator), None) => Right(Item(Isotope(generator), ComponentKind.Generator))
      case (None, Some(microchip)) => Right(Item(Isotope(microchip), ComponentKind.Microchip))
      case _ => Left(parseError(filename, lineNumber, "ambiguous item description"))
    item.map(value => ParsedItem(value, position + matched.end))
end parseItemAt

private def validatePairs(filename: String, floors: Seq[Seq[Item]]): Either[AocError, Facility] =
  val located = floors.zipWithIndex.flatMap { case (items, floor) =>
    items.map(LocatedItem(_, floor))
  }
  located.groupBy(_.item.isotope).toSeq.sortBy(_._1.name)
    .foldLeft[Either[AocError, Seq[PairLocation]]](Right(Seq.empty)) {
      case (result, (isotope, items)) =>
        for
          pairs <- result
          pair <- locatePair(filename, isotope, items)
        yield pairs :+ pair
    }.map(pairs => Facility(canonicalPairs(pairs)))
end validatePairs

private def locatePair(
    filename: String,
    isotope: Isotope,
    items: Seq[LocatedItem],
): Either[AocError, PairLocation] =
  val generators = items.filter(_.item.kind == ComponentKind.Generator)
  val microchips = items.filter(_.item.kind == ComponentKind.Microchip)
  (generators, microchips) match
    case (Seq(generator), Seq(microchip)) => Right(PairLocation(generator.floor, microchip.floor))
    case _                                =>
      val locations = items.map(item => s"floor ${item.floor + 1}").mkString(", ")
      Left(AocError.ParseError(
        filename,
        s"isotope '${isotope.name}' needs exactly one generator and one microchip; found $locations",
      ))
end locatePair

private def parseError(filename: String, lineNumber: Int, message: String): AocError.ParseError =
  AocError.ParseError(filename, s"line $lineNumber: $message")

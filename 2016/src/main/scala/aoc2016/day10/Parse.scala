package aoc2016.day10

import aoc2016.util.AocError

private val InitialPattern = """value ([0-9]+) goes to bot ([0-9]+)""".r

private val RulePattern =
  """bot ([0-9]+) gives low to (bot|output) ([0-9]+) and high to (bot|output) ([0-9]+)""".r

def parse(filename: String, input: String): Either[AocError, Factory] =
  val lines = input.linesIterator.toSeq
  if lines.isEmpty then Left(AocError.ParseError(filename, "expected at least one bot instruction"))
  else
    lines.zipWithIndex.foldLeft[Either[AocError, Factory]](Right(Factory(Map.empty, Map.empty))) {
      case (result, (line, index)) =>
        for
          factory <- result
          instruction <- parseInstruction(filename, line, index + 1)
          updated <- addInstruction(filename, index + 1, factory, instruction)
        yield updated
    }
end parse

private def parseInstruction(
    filename: String,
    line: String,
    lineNumber: Int,
): Either[AocError, Instruction] = line match
  case InitialPattern(chip, bot) =>
    for
      chipValue <- parseNumber(filename, lineNumber, chip, "chip value")
      botNumber <- parseNumber(filename, lineNumber, bot, "bot number")
    yield Instruction.Initial(chipValue, botNumber)
  case RulePattern(bot, lowKind, lowNumber, highKind, highNumber) =>
    for
      botNumber <- parseNumber(filename, lineNumber, bot, "bot number")
      low <- parseDestination(filename, lineNumber, lowKind, lowNumber)
      high <- parseDestination(filename, lineNumber, highKind, highNumber)
    yield Instruction.Rule(botNumber, BotRule(low, high))
  case _ => Left(parseError(filename, lineNumber, "unrecognized bot instruction"))
end parseInstruction

private def parseDestination(
    filename: String,
    lineNumber: Int,
    kind: String,
    numberText: String,
): Either[AocError, Destination] =
  for
    number <- parseNumber(filename, lineNumber, numberText, "destination number")
    destination <- kind match
      case "bot"    => Right(Destination.Bot(number))
      case "output" => Right(Destination.Output(number))
      case _        => Left(parseError(filename, lineNumber, "unrecognized destination"))
  yield destination
end parseDestination

private def parseNumber(
    filename: String,
    lineNumber: Int,
    text: String,
    description: String,
): Either[AocError, Int] = text.toIntOption
  .toRight(parseError(filename, lineNumber, s"$description is outside the integer range"))
end parseNumber

private def addInstruction(
    filename: String,
    lineNumber: Int,
    factory: Factory,
    instruction: Instruction,
): Either[AocError, Factory] = instruction match
  case Instruction.Initial(chip, bot) =>
    val chips = factory.initialChips.getOrElse(bot, Seq.empty)
    if chips.size == 2 then
      Left(parseError(filename, lineNumber, s"bot $bot starts with three chips"))
    else Right(factory.copy(initialChips = factory.initialChips.updated(bot, chips :+ chip)))
  case Instruction.Rule(bot, rule) =>
    if factory.rules.contains(bot) then
      Left(parseError(filename, lineNumber, s"bot $bot has more than one rule"))
    else Right(factory.copy(rules = factory.rules.updated(bot, rule)))
end addInstruction

private def parseError(filename: String, lineNumber: Int, message: String): AocError.ParseError =
  AocError.ParseError(filename, s"line $lineNumber: $message")

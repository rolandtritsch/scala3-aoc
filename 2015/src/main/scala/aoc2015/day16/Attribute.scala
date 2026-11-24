package aoc2015.day16

/** A compound the MFCSAM can detect in a gift sample.
  *
  * The ticker tape lists ten attributes, and each Aunt Sue remembers a count for some of them. The
  * labels are fixed by the puzzle, so this enum is the closed set of every attribute that can
  * appear in either the reading or a Sue's remembered facts.
  *
  * @param label
  *   the attribute's name as it appears in the input.
  */
enum Attribute(val label: String):
  case Children extends Attribute("children")
  case Cats extends Attribute("cats")
  case Samoyeds extends Attribute("samoyeds")
  case Pomeranians extends Attribute("pomeranians")
  case Akitas extends Attribute("akitas")
  case Vizslas extends Attribute("vizslas")
  case Goldfish extends Attribute("goldfish")
  case Trees extends Attribute("trees")
  case Cars extends Attribute("cars")
  case Perfumes extends Attribute("perfumes")
end Attribute

object Attribute:

  /** The attribute whose `label` matches the given text, or `None` when no attribute is named that
    * way.
    */
  def fromLabel(label: String): Option[Attribute] = Attribute.values.find(_.label == label)

end Attribute

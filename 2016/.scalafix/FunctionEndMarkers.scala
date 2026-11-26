package fix

import scala.meta._
import scalafix.lint.LintSeverity
import scalafix.v1._

/** Enforce function end markers after formatting, including expression chains and local functions. */
class FunctionEndMarkers extends SyntacticRule("FunctionEndMarkers") {
  override def fix(implicit doc: SyntacticDocument): Patch = {
    doc.tree.collect { case definition: Defn.Def =>
      // Annotations belong to Defn.Def's position but do not count toward function length.
      val firstLine = definition.tokens
        .collectFirst { case token: Token.KwDef =>
          token.pos.startLine
        }
        .getOrElse(definition.pos.startLine)
      val lines = definition.body.pos.endLine - firstLine + 1
      // Scalameta represents end markers as siblings, not as part of the definition.
      val marker = definition.parent.toList
        .flatMap(_.children)
        .dropWhile(_ ne definition)
        .drop(1)
        .headOption
        .collect {
          case end: Term.EndMarker if end.name.value == definition.name.value => end
        }
      marker match {
        case None if lines >= 6 =>
          Patch.lint(
            Diagnostic(
              id = "missing",
              message =
                s"${definition.name.value} spans $lines lines; add end ${definition.name.syntax}",
              position = definition.name.pos,
              severity = LintSeverity.Error
            )
          )
        case Some(end) if lines <= 5 =>
          Patch.lint(
            Diagnostic(
              id = "unnecessary",
              message = s"${definition.name.value} spans $lines lines; omit its end marker",
              position = end.pos,
              severity = LintSeverity.Error
            )
          )
        case _ => Patch.empty
      }
    }.asPatch
  }
}

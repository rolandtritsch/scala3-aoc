#!/usr/bin/env python3
"""Exercise the actual Scalafix rule against valid and deliberately invalid Scala 3."""

from pathlib import Path
import re
import subprocess
import sys
import tempfile

VALID = '''object Valid:
  /** Documentation does not count toward the function's length.
    * Nor do annotations before the def keyword.
    */
  @deprecated(
    "fixture",
    "test"
  )
  def annotated: Int =
    val next = 1
    next + 1

  def five(n: Int): Int =
    val a = n + 1
    val b = a + 1
    val c = b + 1
    c + 1

  def six(n: Int): Int =
    val a = n + 1
    val b = a + 1
    val c = b + 1
    val d = c + 1
    d + 1
  end six

  def chain(xs: Seq[Int]): Int = xs
    .map(_ + 1)
    .filter(_ > 0)
    .take(10)
    .map(_ * 2)
    .sum
  end chain

  def outer(n: Int): Int =
    def local: Int =
      val a = n + 1
      val b = a + 1
      val c = b + 1
      val d = c + 1
      d + 1
    end local
    local
  end outer

  def literal: String = """
    one
    two
    three
    four
    """
  end literal

  def text: String = "end text" // not an end marker
end Valid

trait Abstract:
  def pending(n: Int): Int
end Abstract
'''

INVALID = '''object Invalid:
  def fiveMarked(n: Int): Int =
    val a = n + 1
    val b = a + 1
    val c = b + 1
    c + 1
  end fiveMarked

  def sixUnmarked(n: Int): Int =
    val a = n + 1
    val b = a + 1
    val c = b + 1
    val d = c + 1
    d + 1

  def chainUnmarked(xs: Seq[Int]): Int = xs
    .map(_ + 1)
    .filter(_ > 0)
    .take(10)
    .map(_ * 2)
    .sum

  @deprecated(
    "fixture",
    "test"
  )
  def annotatedMarked: Int = 1
  end annotatedMarked

  def commentMarked: Int = 1
  // This trailing comment does not make the function longer.
  end commentMarked

  def outer(n: Int): Int =
    def localUnmarked: Int =
      val a = n + 1
      val b = a + 1
      val c = b + 1
      val d = c + 1
      d + 1
    localUnmarked
  end outer
end Invalid
'''

EXPECTED = {
    ("unnecessary", "fiveMarked"),
    ("missing", "sixUnmarked"),
    ("missing", "chainUnmarked"),
    ("unnecessary", "annotatedMarked"),
    ("unnecessary", "commentMarked"),
    ("missing", "localUnmarked"),
}


def main():
    if len(sys.argv) != 2:
        raise SystemExit("Usage: python3 scripts/test_function_end_markers.py YEAR")
    project = Path(sys.argv[1]).resolve()
    if not (project / ".scalafix/FunctionEndMarkers.scala").is_file():
        raise SystemExit(f"No FunctionEndMarkers rule in {project}")
    with tempfile.TemporaryDirectory(prefix="aoc-end-marker-tests-") as directory:
        fixtures = Path(directory)
        (fixtures / "Valid.scala").write_text(VALID)
        (fixtures / "Invalid.scala").write_text(INVALID)
        command = "scalafix --syntactic --check --rules file:.scalafix/FunctionEndMarkers.scala --files "
        result = subprocess.run(
            ["sbt", "--server", "--batch",
             f'set Compile / unmanagedSourceDirectories := Seq(file("{fixtures}"))',
             command + str(fixtures / "Valid.scala"),
             command + str(fixtures / "Invalid.scala")],
            cwd=project, text=True, stdout=subprocess.PIPE, stderr=subprocess.STDOUT,
        )
        diagnostics = re.findall(
            r"\[FunctionEndMarkers\.(missing|unnecessary)\]\s+(\w+) spans", result.stdout
        )
        if result.returncode == 0 or len(diagnostics) != len(EXPECTED) or set(diagnostics) != EXPECTED:
            print(result.stdout)
            raise SystemExit(f"Expected {sorted(EXPECTED)}, received {diagnostics}")
        print(f"{project.name}: end-marker rule accepted valid cases and rejected all six invalid cases")


if __name__ == "__main__":
    main()

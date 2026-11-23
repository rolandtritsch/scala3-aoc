// scala3-aoc-0000 - Advent of Code template in Scala 3.
// Based on the scala3-aoc-2024 (Mill) blueprint, migrated to sbt.

ThisBuild / scalaVersion := "3.9.0"

// Required by sbt-scalafix semantic rules (see .scalafix.conf).
ThisBuild / semanticdbEnabled := true
ThisBuild / scalafixDependencies += "com.github.xuwei-k" %% "scalafix-rules" % "0.6.30"

lazy val root = (project in file(".")).settings(
  name := "scala3-aoc-0000",
  scalacOptions ++= Seq("-Wunused:imports", "-Werror", "-deprecation", "-Wvalue-discard"),
  libraryDependencies ++= Seq(
    "org.scala-lang.modules" %% "scala-parallel-collections" % "1.2.0",
    "org.scala-lang.modules" %% "scala-collection-contrib" % "0.4.0",
    "org.scalanlp" %% "breeze" % "2.1.0",
    "org.tritsch" %% "scala-corner" % "1.0.4",
    "dev.zio" %% "zio" % "2.1.13",
    "dev.zio" %% "zio-test" % "2.1.13" % Test,
    "dev.zio" %% "zio-test-sbt" % "2.1.13" % Test,
  ),
  // Note: `com.eed3si9n.eval` (scala toolbox) from the 2024 blueprint is
  // intentionally dropped: it was only used by Day24 and pins artifacts
  // to an old Scala minor version.
  testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework"),
  Test / fork := true,
  Test / javaOptions ++= Seq("-Xss1G", "-Xmx10G"),
)

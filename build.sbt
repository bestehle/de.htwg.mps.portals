name := "de.htwg.mps.portals"

organization := "de.htwg.mps"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.10.4"

// ScctPlugin.instrumentSettings

resolvers += Classpaths.sbtPluginReleases


libraryDependencies ++= Seq(
  "org.specs2" %% "specs2" % "2.4" % "test",
  "org.scalatest" %% "scalatest" % "2.2.1" % "test",
  "org.scala-lang.modules" % "scala-swing_2.11" % "1.0.1",
  "org.scala-lang" % "scala-reflect" % "2.10.0",
  "com.escalatesoft.subcut" % "subcut_2.10" % "2.1",
  "com.typesafe.akka" % "akka-actor_2.10" % "2.3.8"
)

// EclipseKeys.withSource := true

// EclipseKeys.withJavadoc := true

instrumentSettings

// ScoverageSbtPlugin.ScoverageKeys.coverageExcludedPackages := "de.htwg.mps.portals;de.htwg.mps.portals.actor.*;de.htwg.mps.portals.config.*;de.htwg.mps.portals.util.*"


// no folders for src/main/java and src/test/java
unmanagedSourceDirectories in Compile := (scalaSource in Compile).value :: Nil

unmanagedSourceDirectories in Test := (scalaSource in Test).value :: Nil
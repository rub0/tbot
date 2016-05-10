import _root_.sbt.Keys._

name := "tbot"

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "com.typesafe.akka" % "akka-actor_2.11" % "2.4.4",
  "com.typesafe.akka" % "akka-stream_2.11" % "2.4.4",
  "com.typesafe.akka" % "akka-testkit_2.11" % "2.4.4",
  "com.typesafe" % "config" % "1.3.0",
  "org.scalaj" %% "scalaj-http" % "2.2.0",
  "org.json4s" %% "json4s-native" % "3.2.11",
  "org.scalatest" %% "scalatest" % "2.2.4"
)
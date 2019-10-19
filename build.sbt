
name := "ApexScanner"

version := "1.0"

scalaVersion := "2.12.10"

//https://blog.threatstack.com/useful-scalac-options-for-better-scala-development-part-1
scalacOptions ++= Seq(
    "-target:jvm-1.8",
    "-deprecation",
    "-feature",
    "-unchecked",
    "-encoding", "UTF-8",       // yes, this is 2 args
    "-Xfatal-warnings",
    "-Xlint",
    "-Yno-adapted-args",
    "-Ywarn-numeric-widen",
    "-Ywarn-value-discard",
    "-Xfuture",
    "-Ywarn-unused-import",
    "-Ywarn-dead-code",
    "-Ypartial-unification"
)
// disable generation of scala-<version> folders, we do not need cross compilation
crossPaths := false

sources in doc in Compile := List() // do NOT generate Scaladoc, it is a waste of time

// Get rid of scala-{version} folders
sourceDirectories in Compile ~= ( dirs =>
    dirs.filterNot(_.absolutePath.endsWith("-2.11")).filterNot(_.absolutePath.endsWith("-2.12"))
)

libraryDependencies += "org.antlr" %  "antlr4-runtime" % "4.7.1"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % "test"

// logging
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.1.7"
//libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.21"
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0"

// command line arguments
libraryDependencies += "com.github.scopt" %% "scopt" % "3.5.0"

val circeVersion = "0.7.0"

libraryDependencies ++= Seq(
    "io.circe" %% "circe-core",
    "io.circe" %% "circe-generic",
    //"io.circe" %% "circe-optics",
    "io.circe" %% "circe-parser"
).map(_ % circeVersion)

mainClass in assembly := Some("com.neowit.apexscanner.server.Main")

assemblyMergeStrategy in assembly := {
    case x if x.endsWith(".java") => MergeStrategy.discard
    case "commands.txt" => MergeStrategy.discard
    case x if x.endsWith(".g4") => MergeStrategy.discard
    case x =>
        val oldStrategy = (assemblyMergeStrategy in assembly).value
        oldStrategy(x)
}

//uncomment line below to disable tests during build
test in assembly := {}

//assemblyOption in assembly ~= { _.copy(includeScala = false) }
logLevel in assembly := Level.Error


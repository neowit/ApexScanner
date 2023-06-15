
name := "ApexScanner"

version := "1.0"

//scalaVersion := "2.12.10"
scalaVersion := "2.13.4"

//https://blog.threatstack.com/useful-scalac-options-for-better-scala-development-part-1
scalacOptions ++= Seq(
    "-target:jvm-1.8",
    "-deprecation",
    "-feature",
    "-unchecked",
    "-encoding", "UTF-8",       // yes, this is 2 args
    "-Xfatal-warnings",
    "-Xlint",
    //"-Yno-adapted-args",
    "-Ywarn-numeric-widen",
    "-Ywarn-value-discard",
    //"-Xfuture",
    //"-Ywarn-unused-import",
    "-Ywarn-dead-code",
    //"-Ypartial-unification"
    // Excluding -byname-implicit is required for Scala 2.13 due to https://github.com/scala/bug/issues/12072
    "-Xlint:_,-byname-implicit", // without this parameter circe deriveDecoder results in: Block result was adapted via implicit conversion
)
// disable generation of scala-<version> folders, we do not need cross compilation
crossPaths := false

//sources in doc in Compile := List() // do NOT generate Scaladoc, it is a waste of time
Compile / doc / sources  := List() // do NOT generate Scaladoc, it is a waste of time

// Get rid of scala-{version} folders
Compile / sourceDirectories ~= ( dirs =>
    dirs.filterNot(_.absolutePath.endsWith("-2.11")).filterNot(_.absolutePath.endsWith("-2.12")).filterNot(_.absolutePath.endsWith("-2.13"))
)

libraryDependencies += "org.antlr" %  "antlr4-runtime" % "4.10.1"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.1.1" % "test"

// logging
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"
//libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.21"
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"

// command line arguments
//libraryDependencies += "com.github.scopt" %% "scopt_2.13" % "4.0.0-RC2"
libraryDependencies += "com.github.scopt" % "scopt_2.13" % "4.0.0-RC2"

val circeVersion = "0.12.3"

libraryDependencies ++= Seq(
    "io.circe" %% "circe-core",
    "io.circe" %% "circe-generic",
    //"io.circe" %% "circe-optics",
    "io.circe" %% "circe-parser"
).map(_ % circeVersion)

assembly / mainClass := Some("com.neowit.apexscanner.server.Main")

assembly / assemblyMergeStrategy := {
    case x if x.endsWith(".java") => MergeStrategy.discard
    case "commands.txt" => MergeStrategy.discard
    case x if x.endsWith(".g4") => MergeStrategy.discard
    case x =>
        val oldStrategy = ( assembly / assemblyMergeStrategy ).value
        oldStrategy(x)
}

//uncomment line below to disable tests during build
assembly / test := {}

//assemblyOption in assembly ~= { _.copy(includeScala = false) }
assembly / logLevel := Level.Error


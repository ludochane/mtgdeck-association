name := "mtgdeck-association"

organization := "fr.lcwi"

version := "1.0.0-SNAPSHOT"

scalaVersion := "2.10.6"

resolvers += "Cloudera" at "https://repository.cloudera.com/artifactory/cloudera-repos"


libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "1.6.2" % "provided",
  "org.apache.spark" %% "spark-mllib" % "1.6.2" % "provided",
  "org.apache.spark" %% "spark-sql" % "1.6.2" % "provided",
  "com.databricks" %% "spark-csv" % "1.3.0" % "provided",
  "org.apache.commons" % "commons-lang3" % "3.4",
  "joda-time" % "joda-time" % "2.9.1",
  "org.slf4j" % "slf4j-api" % "1.7.10",
  "com.github.scopt" %% "scopt" % "3.4.0",
  "org.scalatest" %% "scalatest" % "2.2.6" % "test"
)

assemblyMergeStrategy in assembly := {
  case PathList("javax", "servlet", xs@_*) => MergeStrategy.discard
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}

// Add assembly artifact in "sbt publish"
artifact in (Compile, assembly) := {
  val art = (artifact in (Compile, assembly)).value
  art.copy(`classifier` = Some("assembly"))
}

addArtifact(artifact in (Compile, assembly), assembly)

// If not, multiple Spark contexts are spawned
parallelExecution in Test := false
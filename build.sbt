name := "kafka-producer"

version := "0.1"

scalaVersion := "2.12.6"

libraryDependencies ++= Seq(
  "org.apache.kafka"           % "kafka-clients"    % "2.8.0",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.4.0",
  "ch.qos.logback"             % "logback-classic"  % "1.2.3",
  "org.apache.commons"         % "commons-csv"      % "1.8",
  "org.apache.commons"         % "commons-lang3"    % "3.12.0"
)

val circeVersion = "0.12.3"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)

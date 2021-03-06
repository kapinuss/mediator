name := "mediator"
version := "0.1"
organization := "ru.kapinuss"
scalaVersion := "2.12.7"

val akkaVersion = "2.5.19"
val akkaHttpVersion = "10.1.5"
val logbackVersion = "1.2.3"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-stream-kafka" % "1.0-RC1",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.0",
  "com.iheart" %% "ficus" % "1.4.2",
  "org.bouncycastle" % "bcprov-jdk15on" % "1.58",
  "ch.qos.logback" % "logback-classic" % logbackVersion,
  "ch.qos.logback" % "logback-core" % logbackVersion
)

resolvers ++= Seq("Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases/",
  "SonaType" at "https://oss.sonatype.org/content/groups/public",
  "Typesafe maven releases" at "http://repo.typesafe.com/typesafe/maven-releases/",
  "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/")

fork in Test := true
fork in run := true
connectInput in run := true
outputStrategy := Some(StdoutOutput)
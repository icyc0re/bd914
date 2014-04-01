name := "Simple Project"

version := "1.0"

scalaVersion := "2.10.3"

libraryDependencies ++= Seq("org.apache.spark" %% "spark-core" % "0.9.0-incubating",
 "org.scalaj" %% "scalaj-http" % "0.3.14")

libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % "2.0.1",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "com.h2database" % "h2" % "1.3.170",
  "org.xerial" % "sqlite-jdbc" % "3.7.2")

resolvers += "Akka Repository" at "http://repo.akka.io/releases/"

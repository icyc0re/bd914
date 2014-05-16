name := "recommender_module"

resolvers ++= Seq(
  "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/"
)

libraryDependencies ++= Seq(
  ("play" % "play_2.10" % "2.1.0").
    exclude("commons-logging", "commons-logging").
    exclude("play", "sbt-link").
    exclude("ch.qos.logback", "logback-classic").
    exclude("com.typesafe.akka", "akka-actor_2.10").
    exclude("com.typesafe.akka", "akka-slf4j_2.10"),
  "org.scalaj" %% "scalaj-http" % "0.3.15"
)


version := "1.0"
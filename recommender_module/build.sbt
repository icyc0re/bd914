name := "recommender_module"

version := "1.0"

scalaVersion := "2.10.3"

libraryDependencies ++= Seq(
  ("play" % "play_2.10" % "2.1.0").
    exclude("commons-logging", "commons-logging").
    exclude("play", "sbt-link").
    exclude("ch.qos.logback", "logback-classic").
    exclude("com.typesafe.akka", "akka-actor_2.10").
    exclude("com.typesafe.akka", "akka-slf4j_2.10")
)

//UNCOMMENT FOR SPARK BUILD
libraryDependencies ++= Seq(
  ("org.apache.spark" % "spark-core_2.10" % "0.9.1").
    exclude("commons-logging", "commons-logging").
    exclude("org.mortbay.jetty", "servlet-api").
    exclude("commons-beanutils", "commons-beanutils-core").
    exclude("commons-collections", "commons-collections").
    exclude("commons-collections", "commons-collections").
    exclude("com.esotericsoftware.minlog", "minlog").
    exclude("org.apache.hadoop", "hadoop-yarn-common")
)

resolvers ++= Seq(
  "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/",
  "Akka Repository" at "http://repo.akka.io/releases/"
)

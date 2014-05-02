name := "recommender_module"

resolvers ++= Seq(
  "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/"
)

libraryDependencies ++= Seq(
  "play" % "play_2.10" % "2.1.0",
  "org.scalaj" %% "scalaj-http" % "0.3.15"
)


version := "1.0"
    

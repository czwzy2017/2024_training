ThisBuild / scalaVersion := "2.12.19"

ThisBuild / version := "1.0-SNAPSHOT"

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .settings(
    name := """shopping-server""",
    libraryDependencies ++= Seq(
      guice,
      "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % Test,
      "com.typesafe.akka" %% "akka-actor" % "2.6.20",
      "com.typesafe.play" %% "play-akka-http-server" % "2.8.22"
    )
  )
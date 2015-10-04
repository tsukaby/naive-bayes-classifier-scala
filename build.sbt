
lazy val root = (project in file(".")).
  settings(
    organization := "com.tsukaby",
    name := "naive-bayes-classifier-scala",
    version := "0.1.0",
    scalaVersion := "2.11.7",
    libraryDependencies ++= Seq(
      "org.specs2" %% "specs2-core" % "3.6.4" % "test"
    )
  )

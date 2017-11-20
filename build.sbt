import sbt.Keys._

lazy val root = (project in file(".")).
  settings(
    organization := "com.tsukaby",
    name := "naive-bayes-classifier-scala",
    version := "0.1.1",
    scalaVersion := "2.12.4",
    crossScalaVersions := Seq("2.11.12", "2.12.4"),
    libraryDependencies ++= Seq(
      "org.specs2" %% "specs2-core" % "4.0.1" % "test"
    ),
    // Maven deploy settings
    publishMavenStyle := true,
    publishArtifact in Test := false,
    publishTo := {
      val nexus = "https://oss.sonatype.org/"
      if (isSnapshot.value)
        Some("snapshots" at nexus + "content/repositories/snapshots")
      else
        Some("releases" at nexus + "service/local/staging/deploy/maven2")
    },
    pomIncludeRepository := { _ => false},
    pomExtra := <url>https://github.com/tsukaby/naive-bayes-classifier-scala</url>
      <licenses>
        <license>
          <name>MIT License</name>
          <url>http://www.opensource.org/licenses/mit-license.php</url>
          <distribution>repo</distribution>
        </license>
      </licenses>
      <scm>
        <url>git@github.com:tsukaby/naive-bayes-classifier-scala.git</url>
        <connection>scm:git:git@github.com:tsukaby/naive-bayes-classifier-scala.git</connection>
      </scm>
      <developers>
        <developer>
          <id>tsukaby</id>
          <name>Shuya Tsukamoto</name>
          <url>https://github.com/tsukaby</url>
        </developer>
      </developers>,
    // Add warnings
    scalacOptions ++= Seq(
      "-deprecation",
      "-feature",
      "-unchecked",
      "-Xlint",
      "-Ywarn-dead-code",
      "-Ywarn-numeric-widen",
      "-Ywarn-unused",
      "-Ywarn-unused-import",
      "-Ywarn-value-discard",
      "-Xfatal-warnings",
      "-Yrangepos"
    )
  )

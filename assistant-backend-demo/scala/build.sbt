ThisBuild / scalaVersion := "2.13.15" // or your preferred Scala version

lazy val root = (project in file("."))
  .settings(
    organization := "bgl-smartdocs-ai-studio",
    version := "1.0.0",
    // Set Java toolchain to Java 17
    Compile / compile / scalacOptions ++= Seq(),
    Compile / compile / javacOptions ++= Seq("--release", "17"),

    resolvers ++= Seq(
      Resolver.mavenCentral
    ),

    libraryDependencies ++= Seq(
      "org.springframework.boot" % "spring-boot-starter-webflux" % "3.5.0",
      "org.springframework.cloud" % "spring-cloud-gateway-webflux" % "4.2.0",
    ),
)

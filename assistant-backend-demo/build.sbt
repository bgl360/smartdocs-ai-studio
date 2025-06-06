lazy val springBootVersion = "3.4.2"

resolvers += sbt.Resolver.mavenLocal

lazy val  root = (project in file("."))
  .settings(
    inThisBuild(List(
      organization := "com.smartdocs.ai.studio",
      scalaVersion := "2.13.15"
    )),
    name := "bgl-smartdocs-ai-studio",
    version := "1.0",
    libraryDependencies ++= Seq(
      "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5" exclude("org.slf4j", "slf4j-api"),
      "org.springframework.boot" % "spring-boot-starter-webflux" % springBootVersion,
      "org.springframework.boot" % "spring-boot-starter-actuator" % springBootVersion,
      "org.springframework.boot" % "spring-boot-starter-validation" % springBootVersion,
      "org.springframework.boot" % "spring-boot-starter-security" % springBootVersion,
      "org.springframework.boot" % "spring-boot-starter-oauth2-resource-server" % springBootVersion,
      "org.springframework.boot" % "spring-boot-starter-data-r2dbc" % springBootVersion,
      "org.springframework.boot" % "spring-boot-starter-data-redis-reactive" % springBootVersion,
      "org.springframework.cloud" % "spring-cloud-gateway-webflux" % "4.2.0",

      "org.springframework.boot" % "spring-boot-starter-oauth2-client" % springBootVersion,
    ),
//    ThisBuild / scapegoatVersion := "3.1.8"
  )

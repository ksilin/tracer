// *****************************************************************************
// Build settings
// *****************************************************************************

inThisBuild(
  Seq(
    organization     := "default",
    organizationName := "ksilin",
    startYear        := Some(2021),
    licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0")),
    scalaVersion := "2.13.3",
    scalacOptions ++= Seq(
      "-deprecation",
      "-unchecked",
      "future",
      "-language:_",
      "-encoding",
      "UTF-8",
      "-Ywarn-unused:imports"
    ),
    scalafmtOnCompile := true,
    dynverSeparator   := "_", // the default `+` is not compatible with docker tags
  )
)

// *****************************************************************************
// Projects
// *****************************************************************************

lazy val tracer =
  project
    .in(file("."))
    .enablePlugins(AutomateHeaderPlugin)
    .settings(commonSettings)
    .settings(
      libraryDependencies ++= Seq(
        library.clients,
        library.kafka,
        library.tracingClient,
        library.jaegerClient,
        library.betterFiles,
        library.config,
        library.airframeLog,
        library.logback,
        library.scalatest % Test,
      ),
    )

// *****************************************************************************
// Project settings
// *****************************************************************************

lazy val commonSettings =
  Seq(
    // Also (automatically) format build definition together with sources
    Compile / scalafmt := {
      val _ = (Compile / scalafmtSbt).value
      (Compile / scalafmt).value
    },
    resolvers ++= Seq(
      "confluent" at "https://packages.confluent.io/maven/",
      "jitpack" at "https://jitpack.io"
    )
  )

// *****************************************************************************
// Library dependencies
// *****************************************************************************

lazy val library =
  new {
    object Version {
      val kafka       = "3.0.0"
      val cp          = "6.2.0"
      val opentracing = "0.1.15"
      val jaeger      = "1.6.0"
      val betterFiles = "3.9.1"
      val config      = "1.4.1"
      val airframeLog = "20.12.1"
      val scalatest   = "3.2.0"
      val logback     = "1.2.3"
    }
    val clients       = "org.apache.kafka"       % "kafka-clients"            % Version.kafka
    val kafka         = "org.apache.kafka"      %% "kafka"                    % Version.kafka
    val tracingClient = "io.opentracing.contrib" % "opentracing-kafka-client" % Version.opentracing
    val jaegerClient  = "io.jaegertracing"       % "jaeger-client"            % Version.jaeger
    val betterFiles   = "com.github.pathikrit"  %% "better-files"             % Version.betterFiles
    val config        = "com.typesafe"           % "config"                   % Version.config
    val airframeLog   = "org.wvlet.airframe"    %% "airframe-log"             % Version.airframeLog
    val logback       = "ch.qos.logback"         % "logback-classic"          % Version.logback
    val scalatest     = "org.scalatest"         %% "scalatest"                % Version.scalatest
  }

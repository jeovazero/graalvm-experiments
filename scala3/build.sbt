val scala3Version = "3.3.3"
// enablePlugins(GraalVMNativeImagePlugin)

lazy val nativeBuildSettings = Seq(
  nativeImageOptions ++= Seq(
    "--no-fallback",
    "--static",
    "--libc=musl",
    "--verbose",
    "-H:+ReportExceptionStackTraces"
  ),
  nativeImageInstalled := true
)

val deps = Seq(
  "dev.zio" %% "zio" % "2.1.6",
  "dev.zio" %% "zio-json" % "0.6.2",
  "dev.zio" %% "zio-logging" % "2.3.0",
  "com.softwaremill.sttp.client3" %% "zio" % "3.9.7",
  "com.softwaremill.sttp.client3" %% "core" % "3.9.7"
)

lazy val root = project
  .in(file("."))
  .enablePlugins(NativeImagePlugin)
  .settings(
    name := "cli-graal",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    libraryDependencies := deps
  )
  .settings(nativeBuildSettings)

val scala3Version = "3.4.2"
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

lazy val root = project
  .in(file("."))
  .enablePlugins(NativeImagePlugin)
  .settings(
    name := "cli-graal",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version
  )
  .settings(nativeBuildSettings)

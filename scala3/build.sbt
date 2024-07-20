// TODO: use 3.3.4 when released to support -Wunused:imports
val scala3Version = "3.5.0-RC4"
// enablePlugins(GraalVMNativeImagePlugin)

val isLinux = System.getProperty("os.name").toLowerCase.contains("linux")
val addLibcMusl = if (isLinux) Seq("--libc=musl") else Seq.empty

lazy val nativeBuildSettings = Seq(
  nativeImageOptions ++= Seq(
    "--no-fallback",
    "--static",
    "--verbose",
    "-H:+ReportExceptionStackTraces"
  ) ++ addLibcMusl,
  nativeImageInstalled := true
)

val deps = Seq(
  "dev.zio"                       %% "zio"         % "2.1.6",
  "dev.zio"                       %% "zio-json"    % "0.6.2",
  "dev.zio"                       %% "zio-logging" % "2.3.0",
  "com.softwaremill.sttp.client3" %% "zio"         % "3.9.7",
  "com.softwaremill.sttp.client3" %% "core"        % "3.9.7"
)

lazy val root = project
  .in(file("."))
  .enablePlugins(NativeImagePlugin)
  .settings(
    name                := "cli-graal",
    version             := "0.1.0-SNAPSHOT",
    scalaVersion        := scala3Version,
    libraryDependencies := deps,
    scalacOptions ++= Seq("-Wunused:imports")
  )
  .settings(nativeBuildSettings)

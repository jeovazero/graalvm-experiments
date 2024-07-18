package main

import zio._
import zio.logging.ConsoleLoggerConfig
import zio.logging.LogAnnotation
import zio.logging.LogColor
import zio.logging.LogFilter
import zio.logging.LogFormat._
import zio.logging.consoleLogger

object Logger {
  val format =
    timestamp.fixed(23).color(LogColor.MAGENTA)
      |-| text("|")
      |-| level
      |-| text("|")
      |-| line.color(LogColor.GREEN)
      |-| text("|")
      |-| allAnnotations.color(LogColor.YELLOW)

  val console = consoleLogger(
    ConsoleLoggerConfig(format, LogFilter.LogLevelByNameConfig.default)
  )

  val runtimeLayer = Runtime.removeDefaultLoggers ++ console

  def withRequest = LogAnnotation[String]("request", (_, i) => i, _.toString)
  def withBody    = LogAnnotation[String]("body", (_, i) => i, _.toString)
}

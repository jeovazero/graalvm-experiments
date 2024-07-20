package main

import zio._

object App extends ZIOAppDefault {
  override val bootstrap: ZLayer[Any, Nothing, Unit] =
    Logger.runtimeLayer ++ Runtime.enableLoomBasedExecutor.orDie

  def isVerbose(flags: List[Flag]) = flags.contains(Flag.Verbose)

  val run = for {
    args <- getArgs.map(_.toList)
    _    <- args match
              case "myip" :: flags  =>
                Commands.myIp(isVerbose(Flag.extractFlags(flags)))
              case "quote" :: flags =>
                Commands.quote(isVerbose(Flag.extractFlags(flags)))
              case _                => Commands.default
  } yield ()
}

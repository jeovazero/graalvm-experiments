package main

import zio._

object App extends ZIOAppDefault {
  override val bootstrap: ZLayer[Any, Nothing, Unit] =
    Logger.runtimeLayer ++ Runtime.enableLoomBasedExecutor.orDie

  val run = for {
    args <- getArgs.map(_.toList)
    _ <- args match
      case "myip" :: _  => Commands.myIp
      case "quote" :: _ => Commands.quote
      case _            => Commands.default
  } yield ()
}

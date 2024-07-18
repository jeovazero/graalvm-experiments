package main
import sttp.client3._
import zio._

object Commands {
  val default = for {
    _ <- ZIO.log("Application started!")
    _ <- Console.printLine(
      "Available commands:\n    'myip' - current ip address\n    'quote' - random quote from zenquotes"
    )
  } yield ()

  val myIp = Request
    .get[MyIp](uri"https://api.myip.com")
    .flatMap(r => Console.printLine(Util.pretty(r)))

  val quote = Request
    .get[List[Quote]](uri"https://zenquotes.io/api/random")
    .flatMap(r => Console.printLine(Util.pretty(r.head)))
}

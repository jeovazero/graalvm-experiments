import sttp.client3.httpclient.zio.HttpClientZioBackend
import sttp.client3._
import sttp.client3.httpclient.zio._
import sttp.model.Uri
import zio._
import zio.json._
import zio.json.ast._
import zio.logging.LogFormat._
import zio.logging.consoleLogger
import zio.logging.LogFilter
import zio.logging.ConsoleLoggerConfig
import zio.logging.LogColor
import zio.logging.LogAnnotation

def pretty(product: Product): String = {
  val className = product.productPrefix
  val fieldNames = product.productElementNames.toList
  val fieldValues = product.productIterator.toList
  val fields = fieldNames.zip(fieldValues).map { case (name, value) => s"$name = $value"}
  
  fields.mkString(s"$className(", ", ", ")")
}

case class MyIp(ip: String, country: String)
object MyIp {
  implicit val decoder: JsonDecoder[MyIp] = DeriveJsonDecoder.gen
}

case class Quote(@jsonField("q") quote: String, @jsonField("a") author: String)
object Quote {
  implicit val decoder: JsonDecoder[Quote] = DeriveJsonDecoder.gen
}

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

  val layer = Runtime.removeDefaultLoggers ++ console

  def request = LogAnnotation[String]("request", (_, i) => i, _.toString)
  def body = LogAnnotation[String]("body", (_, i) => i, _.toString)
  
}

object App extends ZIOAppDefault {
  def request[Decodable](uri: Uri)(implicit decoder: JsonDecoder[Decodable]) = for {
    backend <- HttpClientZioBackend()
    _ <- ZIO.logInfo("GET") @@ Logger.request(uri.toString) 
    request = basicRequest
      .response(asStringAlways)
      .get(uri)
    response <- backend.send(request)
    body = response.body
    _ <- ZIO.logInfo(s"Raw Body") @@ Logger.body(body)
    decoded <- ZIO.fromEither(body.fromJson[Decodable])
  } yield (decoded)

  override val bootstrap: ZLayer[Any, Nothing, Unit] =
    Logger.layer ++ Runtime.enableLoomBasedExecutor.orDie

  object Mode {
    val default = for {
      _ <- ZIO.log("Application started!")
      _ <- ZIO.attempt {
        println("Hello world!")
        println(msg)
      }
      _ <- Console.printLine("Available commands:\n    'myip' - current ip address\n    'quote' - random quote from zenquotes")
    } yield ()

    val myIp = request[MyIp](uri"https://api.myip.com").flatMap(r => Console.printLine(pretty(r)))
    val quote = request[List[Quote]](uri"https://zenquotes.io/api/random").flatMap(r => Console.printLine(pretty(r.head)))
  }

  val run = for {
    args <- getArgs
    newValue = args.toList
    _ <- newValue match
      case Nil => Mode.default
      case head :: _ =>
        head match
          case "myip"  => Mode.myIp
          case "quote" => Mode.quote
  } yield ()

}

def msg = "I was compiled by Scala 3. :)"

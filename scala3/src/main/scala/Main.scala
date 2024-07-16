import sttp.client3.httpclient.zio.HttpClientZioBackend
import sttp.client3._
import sttp.client3.httpclient.zio._
import zio._

object ZIOApp {
  val runtime = Runtime.default

  val run = Unsafe.unsafe { implicit unsafe =>
    runtime.unsafe.run(req.flatMap(r => ZIO.attempt(println(r.body)))).getOrThrowFiberFailure()
  }
}

def req =
  HttpClientZioBackend().flatMap { backend =>
    val request = basicRequest
      .response(asStringAlways).get(uri"https://httpbin.org/get")
    backend.send(request)
  }


@main def Hello(args: String*): Unit =
  println("Hello world!")
  println(msg)
  ZIOApp.run

def msg = "I was compiled by Scala 3. :)"

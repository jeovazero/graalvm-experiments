package main

import sttp.client3._
import sttp.client3.httpclient.zio._
import sttp.model.Uri
import zio.ZIO
import zio.json._

object Request {

  def get[Decodable](uri: Uri, withLogs: Boolean = false)(implicit decoder: JsonDecoder[Decodable]) = for {
    backend <- HttpClientZioBackend()
    _ <- (ZIO.logInfo("GET") @@ Logger.withRequest(uri.toString)).when(withLogs)
    request = basicRequest.response(asStringAlways).get(uri)
    response <- backend.send(request)
    body = response.body
    _ <- (ZIO.logInfo(s"Raw Body") @@ Logger.withBody(body)).when(withLogs)
    decoded <- ZIO.fromEither(body.fromJson[Decodable])
  } yield (decoded)

}

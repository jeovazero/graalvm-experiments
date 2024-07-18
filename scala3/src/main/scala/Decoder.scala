package main

import zio.json._

case class MyIp(ip: String, country: String)
object MyIp {
  implicit val decoder: JsonDecoder[MyIp] = DeriveJsonDecoder.gen
}

case class Quote(@jsonField("q") quote: String, @jsonField("a") author: String)
object Quote {
  implicit val decoder: JsonDecoder[Quote] = DeriveJsonDecoder.gen
}

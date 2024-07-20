package main
import sttp.client3._
import zio._

object Commands {
  val default = 
    Console.printLine(
      s"""Available commands: 
         |    myip [flags]  - current ip address
         |    quote [flags] - random quote from zenquotes
         |
         |Flags:
         |    -v, --verbose - prints more information
         |""".stripMargin
    )
    
  def myIp(verbose: Boolean = false) = Request
    .get[MyIp](uri"https://api.myip.com", verbose)
    .flatMap(r => Console.printLine(Util.pretty(r)))

  def quote(verbose: Boolean = false) = Request
    .get[List[Quote]](uri"https://zenquotes.io/api/random", verbose)
    .flatMap(r => Console.printLine(Util.pretty(r.head)))
}

enum Flag:
  case Verbose

object Flag {
  def extractFlags(args: List[String]): List[Flag] =
    args.foldLeft(List.empty[Flag]) {
      case (acc, arg)  =>
        accepedFlags.find(_._1.contains(arg)) match {
          case Some((_, flag)) => acc :+ flag
          case None            => acc
        }          
    }
  val accepedFlags = List((List("-v", "--verbose"), Verbose))
}

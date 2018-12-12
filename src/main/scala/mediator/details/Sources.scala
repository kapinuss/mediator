package mediator.details

import java.nio.file.Paths

import akka.NotUsed
import akka.http.scaladsl.Http
import akka.stream.IOResult
import akka.stream.scaladsl.{FileIO, Source}
import akka.util.ByteString

import scala.concurrent.Future
import scala.language.postfixOps
import scala.concurrent.duration._
import scala.io.StdIn

object Sources {

  import mediator.Mediator.system

  def fileSource(file: String): Source[ByteString, Future[IOResult]] = FileIO.fromPath(Paths.get(file))

  def httpSource(port: Int): Source[Http.IncomingConnection, Future[Http.ServerBinding]] =
    Http().bind(interface = "localhost", port = port)

  def listSource[T](list: List[T]): Source[List[T], NotUsed] = Source.single(list)

  def byteStringSource(str: String): Source[ByteString, NotUsed] =
    Source.single(ByteString(str)).initialTimeout(5 seconds)

  def temperedSource(message: String = "Tick"): Source[String, NotUsed] =
    Source.repeat("Tick").throttle(1, 1 second)

  def prompt: Source[String, NotUsed] = Source.fromIterator(() => Iterator.continually(StdIn.readLine))
}

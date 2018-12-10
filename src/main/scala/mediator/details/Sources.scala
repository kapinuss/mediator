package mediator.details

import java.nio.file.Paths
import akka.http.scaladsl.Http
import akka.stream.IOResult
import akka.stream.scaladsl.{FileIO, Source}
import akka.util.ByteString
import scala.concurrent.Future

object Sources {

  import mediator.Mediator.system

  def fileSource(file: String): Source[ByteString, Future[IOResult]] = FileIO.fromPath(Paths.get(file))

  def httpSource(port: Int): Source[Http.IncomingConnection, Future[Http.ServerBinding]] =
    Http().bind(interface = "localhost", port = port)
}

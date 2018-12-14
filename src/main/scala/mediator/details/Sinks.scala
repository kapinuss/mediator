package mediator.details

import java.nio.file.Paths
import akka.stream.IOResult
import akka.stream.scaladsl.{FileIO, Flow, Sink}
import akka.util.ByteString
import scala.concurrent.Future

object Sinks {

  def fileSink(file: String): Sink[ByteString, Future[IOResult]] = FileIO.toPath(Paths.get(file))

}

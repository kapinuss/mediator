package mediator

import java.nio.file.Paths
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{FileIO, Sink, Source}
import akka.http.scaladsl.model.HttpMethods.GET
import scala.concurrent.{ExecutionContextExecutor, Future}

object Mediator extends App {

  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  val serverSource: Source[Http.IncomingConnection, Future[Http.ServerBinding]] =
    Http().bind(interface = "localhost", port = 8085)

  val requestHandler: HttpRequest => HttpResponse = {
    case request@HttpRequest(GET, Uri.Path("/"), _, _, _) =>
      HttpResponse(entity = HttpEntity(ContentTypes.`text/html(UTF-8)`,
        s"<html><body>Hello world, ${request._2}!</body></html>"))

    case HttpRequest(GET, Uri.Path("/crash"), _, _, _) => sys.error("BOOM!")

    case r: HttpRequest =>
      r.discardEntityBytes()
      HttpResponse(404, entity = "Unknown resource!")
  }

  val bindingFuture: Future[Http.ServerBinding] =
    serverSource.to(Sink.foreach { connection =>
      println("Accepted new connection from " + connection.remoteAddress)
      FileIO.toPath(Paths.get("ips.txt"))
      connection.handleWithSyncHandler(requestHandler)
    }).run()
}
package mediator.http

import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpMethods.GET
import akka.http.scaladsl.model._
import akka.stream.scaladsl.{RunnableGraph, Sink}
import mediator.details.Sources
import mediator.http
import scala.concurrent.Future

object HttpRoute {

  import mediator.Mediator.materializer

  val requestHandler: HttpRequest => HttpResponse = {
    case request@HttpRequest(GET, Uri.Path("/"), _, _, _) =>
      HttpResponse(entity = HttpEntity(ContentTypes.`text/html(UTF-8)`,
        s"<html><body>Hello world, ${request._2}!</body></html>"))
    case HttpRequest(GET, Uri.Path("/crash"), _, _, _) => sys.error("BOOM!")
    case r: HttpRequest =>
      r.discardEntityBytes()
      HttpResponse(404, entity = "Unknown resource!")
  }

  val server: RunnableGraph[Future[Http.ServerBinding]] =
    Sources.httpSource(port = 8080).to(Sink.foreach { connection =>
      println("Accepted new connection from " + connection.remoteAddress)
      connection.handleWithSyncHandler(http.HttpRoute.requestHandler)
    })

  Sources.prompt.to(Sink.foreach(each => println(each))).run()

}

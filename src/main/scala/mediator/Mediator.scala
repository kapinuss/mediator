package mediator

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink
import mediator.details.{Flows, Sinks, Sources}
import scala.concurrent.ExecutionContextExecutor

object Mediator extends App {

  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  val bindingFuture =
    Sources.httpSource(8083).to(Sink.foreach { connection =>
      println("Accepted new connection from " + connection.remoteAddress)
      connection.handleWithSyncHandler(http.HttpRoute.requestHandler)
    }).run()

  val source = Sources.listSource(List(1, 3, 6))

}
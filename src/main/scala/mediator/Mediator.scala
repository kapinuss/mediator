package mediator

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ClosedShape}
import akka.stream.scaladsl.{Broadcast, GraphDSL, RunnableGraph, Sink}
import akka.util.ByteString
import mediator.details.{Sinks, Sources}
import scala.concurrent.ExecutionContextExecutor

object Mediator extends App {

  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  val bindingFuture = Sources.httpSource(8080).to(Sink.foreach { connection =>
      println("Accepted new connection from " + connection.remoteAddress)
      connection.handleWithSyncHandler(http.HttpRoute.requestHandler)
    })


  val graph = RunnableGraph.fromGraph(GraphDSL.create() { implicit builder: GraphDSL.Builder[NotUsed] =>
    import GraphDSL.Implicits._

    val broadcast = builder.add(Broadcast[ByteString](2))
    val source = Sources.byteString("test")

    source ~> broadcast ~> Sinks.fileSink("test1.txt")
    broadcast ~> Sinks.fileSink("test2.txt")

    ClosedShape
  })

}
package mediator

import akka.NotUsed
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpMethods, HttpRequest}
import akka.stream.{ActorMaterializer, ClosedShape}
import akka.stream.scaladsl.{GraphDSL, Merge, RunnableGraph, Sink}
import mediator.details.{Flows, Sources}
import scala.concurrent.ExecutionContextExecutor
import scala.util.{Failure, Success}

object Mediator extends App {

  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  val graph = RunnableGraph.fromGraph(GraphDSL.create() { implicit builder: GraphDSL.Builder[NotUsed] =>
    import GraphDSL.Implicits._

    val mergeStrings = builder.add(Merge[String](1))

    Sources.prompt ~> mergeStrings ~> Flows.strToBS ~> Sink.ignore

    ClosedShape
  })

  def selfRequest(string: String = ""): Unit = Http()
    .singleRequest(HttpRequest(method = HttpMethods.GET, uri = "http://0.0.0.0/"))
    .onComplete {
      case Success(res) => println(res)
      case Failure(_) =>
    }


}
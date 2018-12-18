package mediator

import akka.NotUsed
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpMethods, HttpRequest}
import akka.kafka.ProducerSettings
import akka.stream.{ActorMaterializer, ClosedShape}
import akka.stream.scaladsl.{GraphDSL, Merge, RunnableGraph, Sink}
import mediator.details.{Flows, Sources}
import mediator.utils.Settings
import mediator.http.HttpRoute
import org.apache.kafka.common.serialization.StringSerializer

import scala.concurrent.ExecutionContextExecutor
import scala.util.{Failure, Success}

object Mediator extends App {

  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  lazy val settings = Settings.settings

  HttpRoute.server.run

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

  val config = system.settings.config.getConfig("akka.kafka.producer")
  val producerSettings =
    ProducerSettings(config, new StringSerializer, new StringSerializer)
      .withBootstrapServers(bootstrapServers)
}
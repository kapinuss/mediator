package mediator

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ClosedShape}
import akka.stream.scaladsl.{Broadcast, GraphDSL, Merge, RunnableGraph}
import akka.util.ByteString
import mediator.details.{Flows, Sinks, Sources}
import scala.concurrent.ExecutionContextExecutor

object Mediator extends App {

  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  val graph = RunnableGraph.fromGraph(GraphDSL.create() { implicit builder: GraphDSL.Builder[NotUsed] =>
    import GraphDSL.Implicits._

    val broadcast = builder.add(Broadcast[ByteString](2))
    val merge = builder.add(Merge[ByteString](4))
    val sourceFile = Sources.fileSource("example1.txt")
    val sourceFile2 = Sources.fileSource("example2.txt")

    Sources.temperedSource() ~> Flows.strToBS ~> merge
    sourceFile2 ~> merge
    Sources.byteStringSource("----------------") ~> merge
    sourceFile ~> merge ~> broadcast ~> Sinks.fileSink("result1.txt")
    broadcast ~> Sinks.fileSink("result2.txt")

    ClosedShape
  }).run()

}
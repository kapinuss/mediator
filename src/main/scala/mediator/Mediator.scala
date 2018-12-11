package mediator

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ClosedShape}
import akka.stream.scaladsl.{Broadcast, GraphDSL, Merge, RunnableGraph}
import akka.util.ByteString
import mediator.details.{Sinks, Sources}
import scala.concurrent.ExecutionContextExecutor

object Mediator extends App {

  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  val graph = RunnableGraph.fromGraph(GraphDSL.create() { implicit builder: GraphDSL.Builder[NotUsed] =>
    import GraphDSL.Implicits._

    val broadcast = builder.add(Broadcast[ByteString](2))
    val merge = builder.add(Merge[ByteString](5))
    val sourceSingleString = Sources.byteString("")
    val sourceFile = Sources.fileSource("")
    val sourceFile2 = Sources.fileSource("")
    val sourceSingleString2 = Sources.byteString("")
    val sourceSingleString3 = Sources.byteString("")

    sourceFile ~> merge ~> broadcast ~> Sinks.fileSink("")
    sourceSingleString ~> merge
    sourceSingleString2 ~> merge
    sourceSingleString3 ~> merge
    sourceFile2 ~> merge
    broadcast ~> Sinks.fileSink("")

    ClosedShape
  }).run

}
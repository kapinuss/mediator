package mediator.details

import akka.NotUsed
import akka.stream.scaladsl.Flow
import akka.util.ByteString

object Flows {

  val strToBS: Flow[String, ByteString, NotUsed] = Flow[String].map(s => ByteString(s + "\n"))
}

package mediator.utils

import akka.NotUsed
import akka.kafka.ProducerMessage
import org.apache.kafka.clients.producer.ProducerRecord

class MQ (settings: Settings) {

  val single: ProducerMessage.Envelope[String, String, NotUsed] =
    ProducerMessage.single(new ProducerRecord("topicName", "key", "value"), NotUsed)

}

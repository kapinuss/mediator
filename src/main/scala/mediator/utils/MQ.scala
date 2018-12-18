package mediator.utils

import java.text.SimpleDateFormat
import java.util.concurrent.Future
import java.util.{Calendar, Properties}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord, RecordMetadata}
import org.apache.kafka.common.serialization.StringSerializer

class MQ (settings: Settings) {

  import KafkaActor.KafkaMessage

  val formatter: SimpleDateFormat = new SimpleDateFormat("yyyy:mm:dd:HH:mm:ss")
  def getDateTimeNow: String = formatter.format(Calendar.getInstance().getTime)
  def assembleFullMessage(level: String, message: String): String = settings.network.nodeName + " - " + getDateTimeNow +
    " - " + level + " - " + message

  val kafkaParams: Properties = new Properties
  kafkaParams.put("bootstrap.servers", settings.kafka.map(_.kafkaBrokers))
  kafkaParams.put("key.serializer", classOf[StringSerializer])
  kafkaParams.put("value.serializer", classOf[StringSerializer])
  kafkaParams.put("group.id", settings.kafka.map(_.groupId))

  val producer: KafkaProducer[String,String] = new KafkaProducer[String,String](kafkaParams)

  def message(level: String, message: String): Future[RecordMetadata] = {
    producer.send(new ProducerRecord[String,String](settings.kafka.map(_.topicName)
      .getOrElse(throw new RuntimeException("topicName not specified")), java.util.UUID.randomUUID().toString,
      assembleFullMessage(level, message)))
  }

}

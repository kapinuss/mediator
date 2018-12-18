package mediator.utils

import com.typesafe.config.ConfigFactory
import net.ceedubs.ficus.Ficus._
import net.ceedubs.ficus.readers.ArbitraryTypeReader._

case class Settings(port: Int)

object Settings {

  val settings: Settings =
    ConfigFactory.load("local.conf").withFallback(ConfigFactory.load).as[Settings]("mediator")
}

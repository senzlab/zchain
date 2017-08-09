package com.score.zchain.config

import com.typesafe.config.ConfigFactory

import scala.util.Try

/**
  * Load configurations define in application.conf from here
  *
  * @author eranga herath(erangaeb@gmail.com)
  */
trait AppConf {
  // config object
  val appConf = ConfigFactory.load()

  // senzie config
  lazy val senzieMode = Try(appConf.getString("senzie.mode")).getOrElse("DEV")
  lazy val senzieName = Try(appConf.getString("sensie.name")).getOrElse("sdbltrans")

  // server config
  lazy val switchName = Try(appConf.getString("switch.name")).getOrElse("senzswitch")
  lazy val switchHost = Try(appConf.getString("switch.host")).getOrElse("dev.localhost")
  lazy val switchPort = Try(appConf.getInt("switch.port")).getOrElse(7070)

  // keys config
  lazy val keysDir = Try(appConf.getString("keys.dir")).getOrElse(".keys")
  lazy val publicKeyLocation = Try(appConf.getString("keys.public-key-location")).getOrElse(".keys/id_rsa.pub")
  lazy val privateKeyLocation = Try(appConf.getString("keys.private-key-location")).getOrElse(".keys/id_rsa")
}

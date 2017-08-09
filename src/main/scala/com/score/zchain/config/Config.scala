package com.score.zchain.config

import com.typesafe.config.ConfigFactory

import scala.util.Try

/**
  * Load configurations define in application.conf from here
  *
  * @author eranga herath(erangaeb@gmail.com)
  */
trait Config {
  // config object
  val config = ConfigFactory.load()

  // cassandra config
  lazy val cassandraKeyspace = Try(config.getString("cassandra.keyspace")).getOrElse("zchain")
  lazy val cassandraHost = Try(config.getString("cassandra.host")).getOrElse("dev.localhost")
  lazy val cassandraPort = Try(config.getInt("cassandra.port")).getOrElse(9042)
}

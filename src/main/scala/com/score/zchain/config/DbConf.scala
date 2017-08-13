package com.score.zchain.config

import com.typesafe.config.ConfigFactory

import scala.util.Try

/**
  * Load db configurations define in database.conf from here
  *
  * @author eranga herath(erangaeb@gmail.com)
  */
trait DbConf {
  // config object
  val dbConf = ConfigFactory.load("database.conf")

  // cassandra config
  lazy val cassandraKeyspace = Try(dbConf.getString("db.cassandra.keyspace")).getOrElse("zchain")
  lazy val cassandraHost = Try(dbConf.getString("db.cassandra.host")).getOrElse("dev.localhost")
  lazy val cassandraPort = Try(dbConf.getInt("db.cassandra.port")).getOrElse(9042)
}

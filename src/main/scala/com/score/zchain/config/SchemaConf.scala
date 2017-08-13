package com.score.zchain.config

import com.typesafe.config.ConfigFactory

import scala.util.Try

trait SchemaConf {
  // config object
  val schemaConf = ConfigFactory.load("schema.conf")

  // cassandra config
  lazy val schemaCreateKeyspace = Try(schemaConf.getString("schema.createKeyspace")).getOrElse("")
  lazy val schemaCreateTypeTransaction = Try(schemaConf.getString("schema.createTypeTransaction")).getOrElse("")
  lazy val schemaCreateTypeSignature = Try(schemaConf.getString("schema.createTypeSignature")).getOrElse("")
  lazy val schemaCreateTableTransactions = Try(schemaConf.getString("schema.createTableTransactions")).getOrElse("")
  lazy val schemaCreateTableBlocks = Try(schemaConf.getString("schema.createTableBlocks")).getOrElse("")
}

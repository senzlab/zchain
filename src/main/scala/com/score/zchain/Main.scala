package com.score.zchain

import akka.actor.ActorSystem
import com.score.zchain.actor.BlockCreator
import com.score.zchain.actor.BlockCreator.Create
import com.score.zchain.util.{DbFactory, SenzFactory}

object Main extends App {

  implicit val system = ActorSystem("senz")

  // setup logging
  SenzFactory.setupLogging()

  // setup keys
  SenzFactory.setupKeys()

  // setup db
  DbFactory.initDb()

  // start watcher actor
  val creator = system.actorOf(BlockCreator.props, name = "BlockCreator")
  creator ! Create

}

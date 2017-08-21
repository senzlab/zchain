package com.score.zchain

import akka.actor.ActorSystem
import com.score.zchain.actor.BlockCreator
import com.score.zchain.actor.BlockCreator.Create
import com.score.zchain.util.{DbFactory, SenzFactory}

object Main extends App {

  // first
  //  1. setup logging
  //  2. setup keys
  //  3. setup db
  SenzFactory.setupLogging()
  SenzFactory.setupKeys()
  DbFactory.initDb()

  // start block creator
  implicit val system = ActorSystem("senz")
  system.actorOf(BlockCreator.props, name = "BlockCreator") ! Create

}

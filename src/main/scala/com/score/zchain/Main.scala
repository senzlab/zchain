package com.score.zchain

import akka.actor.ActorSystem
import com.score.zchain.actor.{BlockCreator, BlockSigner, SenzActor}
import com.score.zchain.util.{DbFactory, ZchainFactory}

object Main extends App {

  // first
  //  1. setup logging
  //  2. setup keys
  //  3. setup db
  ZchainFactory.setupLogging()
  ZchainFactory.setupKeys()
  DbFactory.initDb()

  // start senz, block creator, block signer
  implicit val system = ActorSystem("senz")
  system.actorOf(SenzActor.props, name = "SenzActor")
  system.actorOf(BlockCreator.props, name = "BlockCreator")
  //system.actorOf(BlockSigner.props, name = "BlockSigner")

}

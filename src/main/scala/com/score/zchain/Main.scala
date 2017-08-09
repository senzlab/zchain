package com.score.zchain

import akka.actor.ActorSystem
import com.score.zchain.actor.TransactionWatcher
import com.score.zchain.actor.TransactionWatcher.Watch
import com.score.zchain.util.SenzFactory

object Main extends App {

  implicit val system = ActorSystem("senz")

  // setup logging
  SenzFactory.setupLogging()

  // start watcher actor
  val watcher = system.actorOf(TransactionWatcher.props, name = "TransactionWatcher")
  watcher ! Watch

}

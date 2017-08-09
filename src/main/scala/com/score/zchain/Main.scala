package com.score.zchain

import akka.actor.ActorSystem
import com.score.zchain.actor.TransactionWatcher
import com.score.zchain.actor.TransactionWatcher.Watch

object Main extends App {

  implicit val system = ActorSystem("senz")

  // start watcher actor
  val watcher = system.actorOf(TransactionWatcher.props, name = "TransactionWatcher")
  watcher ! Watch

}

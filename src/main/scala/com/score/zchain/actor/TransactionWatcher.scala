package com.score.zchain.actor

import akka.actor.{Actor, Props}
import util.SenzLogger

import scala.concurrent.duration._

object TransactionWatcher {

  case class Watch()

  def props = Props(classOf[TransactionWatcher])

}

class TransactionWatcher extends Actor with SenzLogger {

  import TransactionWatcher._
  import context._

  override def preStart() = {
    logger.debug("Start actor: " + context.self.path)
  }

  override def receive = {
    case Watch =>
      // read transactions and create blocks
      println("hooooo...")

      // reschedule to watch
      context.system.scheduler.scheduleOnce(20.seconds, self, Watch)
  }
}

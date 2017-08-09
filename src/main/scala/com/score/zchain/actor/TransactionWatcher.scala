package com.score.zchain.actor

import akka.actor.{Actor, Props}
import com.score.zchain.comp.{CassandraClusterComp, ChainDbCompImpl}
import com.score.zchain.util.SenzLogger

import scala.concurrent.duration._

object TransactionWatcher {

  case class Watch()

  def props = Props(classOf[TransactionWatcher])

}

class TransactionWatcher extends Actor with ChainDbCompImpl with CassandraClusterComp with SenzLogger {

  import TransactionWatcher._
  import context._

  override def preStart() = {
    logger.debug("Start actor: " + context.self.path)
  }

  override def receive = {
    case Watch =>
      // read transactions and create blocks
      println("hooooo...")

      // todo 1. take transactions from db and create block

      // todo 2. start another actor to sign the block

      // todo 3. send GET #sign <block_id> msg to every peer and wait till sign response coming

      // todo 4. when all peers sing the block make block as confirmed

      // reschedule to watch
      context.system.scheduler.scheduleOnce(20.seconds, self, Watch)
  }
}

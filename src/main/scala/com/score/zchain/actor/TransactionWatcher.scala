package com.score.zchain.actor

import akka.actor.{Actor, Props}
import com.score.zchain.actor.BlockSigner.Sign
import com.score.zchain.comp.{CassandraClusterComp, ChainDbCompImpl}
import com.score.zchain.config.AppConf
import com.score.zchain.protocol.{Block, Transaction}
import com.score.zchain.util.SenzLogger

import scala.concurrent.duration._
import scala.util.Random

object TransactionWatcher {

  case class Watch()

  def props = Props(classOf[TransactionWatcher])

}

class TransactionWatcher extends Actor with ChainDbCompImpl with CassandraClusterComp with AppConf with SenzLogger {

  import TransactionWatcher._
  import context._

  override def preStart() = {
    logger.debug("Start actor: " + context.self.path)
  }

  override def receive = {
    case Watch =>
      // for testing purpose we create trans here
      chainDb.createTransaction(Transaction(senzieName, Random.nextInt(1000), "4344555", "755555", 340, 899322L))

      // take transactions from db and create block
      val trans = chainDb.getTransactions
      if (trans.nonEmpty) {
        val block = Block(senzieName, Random.nextInt(1000), trans, List(), 5644444)
        chainDb.createBlock(block)

        // start another actor to sign the block
        val signer = context.actorOf(BlockSigner.props)
        signer ! Sign(Option(block), None, None)

        // TODO 3. send GET #sign <block_id> msg to every peer and wait till sign response coming

        // TODO 4. when all peers sing the block, mark block as confirmed
      }

      // reschedule to watch
      context.system.scheduler.scheduleOnce(20.seconds, self, Watch)
  }
}

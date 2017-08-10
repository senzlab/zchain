package com.score.zchain.actor

import akka.actor.{Actor, Props}
import com.score.zchain.actor.BlockSigner.Sign
import com.score.zchain.comp.{CassandraClusterComp, ChainDbCompImpl}
import com.score.zchain.config.AppConf
import com.score.zchain.protocol.{Block, Transaction}
import com.score.zchain.util.SenzLogger

import scala.concurrent.duration._

object BlockCreator {

  case class Create()

  def props = Props(classOf[BlockCreator])

}

class BlockCreator extends Actor with ChainDbCompImpl with CassandraClusterComp with AppConf with SenzLogger {

  import BlockCreator._
  import context._

  override def preStart() = {
    logger.debug("Start actor: " + context.self.path)
  }

  override def receive = {
    case Create =>
      // for testing purpose we create trans here
      // TODO remove this
      chainDb.createTransaction(Transaction(bankId = senzieName, from = "4344555", to = "755555", amount = 340, timestamp = 899322L))

      // take transactions from db and create block
      val trans = chainDb.getTransactions
      if (trans.nonEmpty) {
        val block = Block(bankId = senzieName, transactions = trans, timestamp = 5644444)
        chainDb.createBlock(block)

        // start another actor to sign the block
        val signer = context.actorOf(BlockSigner.props)
        signer ! Sign(Option(block), None, None)

        // TODO 3. send GET #sign <block_id> senz to every peer and wait till sign response coming

        // TODO 4. when all peers sing the block, mark block as confirmed

        // TODO 5. delete all transaction saved in the block from transactions table
      }

      // reschedule to create
      context.system.scheduler.scheduleOnce(20.seconds, self, Create)
  }
}

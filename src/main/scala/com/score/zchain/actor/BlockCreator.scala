package com.score.zchain.actor

import akka.actor.{Actor, Props}
import com.score.zchain.actor.BlockSigner.{Sign, SignResp}
import com.score.zchain.comp.ChainDbCompImpl
import com.score.zchain.config.AppConf
import com.score.zchain.protocol.{Balance, Block, Transaction}
import com.score.zchain.util.{BlockFactory, SenzLogger}

import scalaz.Scalaz._
import scala.concurrent.duration._

object BlockCreator {

  case class Create()

  def props = Props(classOf[BlockCreator])

}

class BlockCreator extends Actor with ChainDbCompImpl with AppConf with SenzLogger {

  import BlockCreator._
  import context._

  override def preStart(): Unit = {
    logger.debug("Start actor: " + context.self.path)
  }

  override def receive: Receive = {
    case Create =>
      // take transactions from db and create block
      val trans = chainDb.getTransactions
      if (trans.nonEmpty) {
        // calculate balance
        val cBals = trans.foldMap {
          case Transaction(_, _, from, to, am, _) =>
            Map(from -> (am, 0), to -> (0, am))
        }.map {
          case (name, (in, out)) =>
            Balance(name, in, out)
        }.toList

        val block = Block(bankId = senzieName, hash = BlockFactory.markleHash(trans), transactions = trans, balances = cBals, timestamp = System.currentTimeMillis)
        chainDb.createBlock(block)

        logger.debug("block created, send to sign ")

        // start another actor to sign the block
        val signer = context.actorOf(BlockSigner.props)
        signer ! Sign(Option(block), None, None)

        // delete all transaction saved in the block from transactions table
        chainDb.deleteTransactions(block.transactions)
      } else {
        // reschedule to create
        logger.debug("No transactions, reschedule " + context.self.path)
        context.system.scheduler.scheduleOnce(40.seconds, self, Create)
      }
    case SignResp(Some(block), _, _, signed) =>
      // TODO send GET #sign <block_id> senz to every peer and wait till sign response coming

      // reschedule to create
      logger.debug("Signed block, reschedule " + context.self.path)
      context.system.scheduler.scheduleOnce(20.seconds, self, Create)
    case SignResp(None, bankId, blockId, signed) =>
    // TODO when all peers sing the block, mark block as confirmed
  }
}

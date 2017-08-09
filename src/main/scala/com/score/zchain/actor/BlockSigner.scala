package com.score.zchain.actor

import akka.actor.{Actor, Props}
import com.score.zchain.comp.{CassandraClusterComp, ChainDbCompImpl}
import com.score.zchain.protocol.Block
import com.score.zchain.util.SenzLogger

object BlockSigner {

  case class Sign(block: Option[Block], bankId: Option[String], blockId: Option[Int])

  case class SignResp(block: Block, signed: Boolean)

  def props = Props(classOf[BlockSigner])

}

class BlockSigner extends Actor with ChainDbCompImpl with CassandraClusterComp with SenzLogger {

  import BlockSigner._

  override def preStart() = {
    logger.debug("Start actor: " + context.self.path)
  }

  override def receive = {
    case Sign(Some(block), _, _) =>
      // sing block
      sender ! SignResp(block, signed = true)
    case Sign(None, Some(bankId), Some(blockId)) =>
      // extract block from db and sign
  }

}

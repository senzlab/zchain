package com.score.zchain.actor

import akka.actor.{Actor, Props}
import com.score.zchain.comp.{CassandraClusterComp, ChainDbCompImpl}
import com.score.zchain.protocol.{Block, Signature}
import com.score.zchain.util.SenzLogger

import scala.util.Random

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

      // update signature in db
      chainDb.updateBlockSignature(block, Signature("sampatha", s"sig${Random.nextInt(1000)}"))

      // response back
      sender ! SignResp(block, signed = true)

      // stop self
      context.stop(self)
    case Sign(None, Some(bankId), Some(blockId)) =>
      // extract block from db and sign

      context.stop(self)
  }

}

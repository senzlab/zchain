package com.score.zchain.actor

import java.util.UUID

import akka.actor.{Actor, Props}
import com.score.zchain.comp.ChainDbCompImpl
import com.score.zchain.config.AppConf
import com.score.zchain.protocol.{Block, Msg, Signature}
import com.score.zchain.util.{RSAFactory, SenzFactory, SenzLogger}

object BlockSigner {

  case class Sign(block: Option[Block], bankId: Option[String], blockId: Option[String])

  case class SignResp(block: Option[Block], bankId: Option[String], blockId: Option[String], signed: Boolean)

  def props = Props(classOf[BlockSigner])

}

class BlockSigner extends Actor with ChainDbCompImpl with AppConf with SenzLogger {

  import BlockSigner._

  val senzActor = context.actorSelection("/user/SenzActor")

  override def preStart(): Unit = {
    logger.debug("Start actor: " + context.self.path)
  }

  override def receive: Receive = {
    case Sign(Some(block), _, _) =>
      // sign block hash
      val sig = RSAFactory.sign(block.hash)

      // update signature in db
      chainDb.updateBlockSignature(block, Signature(senzieName, sig))

      // broadcast senz about the new block
      senzActor ! Msg(SenzFactory.blockSenz(block.id.toString))

      // stop self
      context.stop(self)
    case Sign(None, Some(bankId), Some(blockId)) =>
      // extract block from db
      chainDb.getBlock(bankId, UUID.fromString(blockId)) match {
        case Some(b) =>
          // sign block hash
          val sig = RSAFactory.sign(b.hash)

          // update signature in db
          chainDb.updateBlockSignature(b, Signature(senzieName, sig))

          // response back signed = true
          senzActor ! Msg(SenzFactory.blockSignSenz(blockId.toString, bankId, signed = true))
        case None =>
          // response back signed = false
          senzActor ! Msg(SenzFactory.blockSignSenz(blockId.toString, bankId, signed = false))
      }

      context.stop(self)
  }

}

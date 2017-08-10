package com.score.zchain.actor

import java.util.UUID

import akka.actor.{Actor, Props}
import com.score.zchain.comp.{CassandraClusterComp, ChainDbCompImpl}
import com.score.zchain.config.AppConf
import com.score.zchain.protocol.{Block, Signature}
import com.score.zchain.util.SenzLogger

import scala.util.Random

object BlockSigner {

  case class Sign(block: Option[Block], bankId: Option[String], blockId: Option[UUID])

  case class SignResp(block: Option[Block], bankId: Option[String], blockId: Option[UUID], signed: Boolean)

  def props = Props(classOf[BlockSigner])

}

class BlockSigner extends Actor with ChainDbCompImpl with CassandraClusterComp with AppConf with SenzLogger {

  import BlockSigner._

  override def preStart() = {
    logger.debug("Start actor: " + context.self.path)
  }

  override def receive = {
    case Sign(Some(block), _, _) =>
      // TODO generate signature of the block
      val sig = s"sig${Random.nextInt(1000)}"

      // update signature in db
      chainDb.updateBlockSignature(block, Signature(senzieName, sig))

      // response back signed = true
      sender ! SignResp(Option(block), None, None, signed = true)

      // stop self
      context.stop(self)
    case Sign(None, Some(bankId), Some(blockId)) =>
      // extract block from db
      chainDb.getBlock(bankId, blockId) match {
        case Some(b) =>
          // TODO generate signature of the block
          val sig = s"sig${Random.nextInt(1000)}"

          // update signature in db
          chainDb.updateBlockSignature(b, Signature(senzieName, sig))

          // response back signed = true
          sender ! SignResp(None, Option(bankId), Option(blockId), signed = true)
        case None =>
          // response back signed = false
          sender ! SignResp(None, Option(bankId), Option(blockId), signed = false)
      }

      context.stop(self)
  }

}

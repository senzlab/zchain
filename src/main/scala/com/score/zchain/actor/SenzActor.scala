package com.score.zchain.actor

import java.net.{InetAddress, InetSocketAddress}

import akka.actor.SupervisorStrategy.Stop
import akka.actor.{Actor, ActorRef, OneForOneStrategy, Props}
import akka.io.Tcp._
import akka.io.{IO, Tcp}
import akka.util.ByteString
import com.score.zchain.actor.BlockCreator.Create
import com.score.zchain.actor.BlockSigner.{Sign, SignResp}
import com.score.zchain.config.AppConf
import com.score.zchain.protocol.{Msg, Senz, SenzType}
import com.score.zchain.util.{RSAFactory, SenzFactory, SenzLogger, SenzParser}

object SenzActor {

  def props: Props = Props(new SenzActor)

}

class SenzActor extends Actor with AppConf with SenzLogger {

  import context._

  val blockCreator = context.actorSelection("/user/BlockCreator")

  // connect to senz tcp
  val remoteAddress = new InetSocketAddress(InetAddress.getByName(switchHost), switchPort)
  IO(Tcp) ! Connect(remoteAddress)

  override def preStart(): Unit = {
    logger.debug("Start actor: " + context.self.path)
  }

  override def supervisorStrategy = OneForOneStrategy() {
    case e: Exception =>
      logger.error("Exception caught, [STOP ACTOR] " + e)
      logError(e)

      // TODO send error status back

      // stop failed actors here
      Stop
  }

  override def receive: Receive = {
    case Connected(_, _) =>
      logger.debug("TCP connected")

      // tcp conn
      val connection = sender()
      connection ! Register(self)

      // send reg message
      val regSenzMsg = SenzFactory.regSenz
      val senzSignature = RSAFactory.sign(regSenzMsg.trim.replaceAll(" ", ""))
      val signedSenz = s"$regSenzMsg $senzSignature"
      connection ! Write(ByteString(s"$signedSenz;"))

      // wait register
      context.become(registering(connection))
    case CommandFailed(_: Connect) =>
      // failed to connect
      logger.error("CommandFailed[Failed to connect]")
  }

  def registering(connection: ActorRef): Receive = {
    case CommandFailed(_: Write) =>
      logger.error("CommandFailed[Failed to write]")
    case Received(data) =>
      val senzMsg = data.decodeString("UTF-8")
      logger.debug("Received senzMsg : " + senzMsg)

      if (!senzMsg.equalsIgnoreCase("TIK;")) {
        // wait for REG status
        // parse senz first
        val senz = SenzParser.parseSenz(senzMsg)
        senz match {
          case Senz(SenzType.DATA, `switchName`, _, attr, _) =>
            attr.get("#status") match {
              case Some("REG_DONE") =>
                logger.info("Registration done")

                // start block creator and signer
                blockCreator ! Create

                // senz listening
                context.become(listening(connection))
              case Some("REG_ALR") =>
                logger.info("Already registered, continue system")

                // start block creator and signer
                blockCreator ! Create

                // senz listening
                context.become(listening(connection))
              case Some("REG_FAIL") =>
                logger.error("Registration fail, stop system")
                context.stop(self)
              case other =>
                logger.error("UNSUPPORTED DATA message " + other)
            }
          case _ =>
            logger.debug(s"Not support other messages $senzMsg this stats")
        }
      }
  }

  def listening(connection: ActorRef): Receive = {
    case CommandFailed(_: Write) =>
      logger.error("CommandFailed[Failed to write]")
    case Received(data) =>
      val senzMsg = data.decodeString("UTF-8")
      logger.debug("Received senzMsg : " + senzMsg)

      if (!senzMsg.equalsIgnoreCase("TIK;")) {
        // only handle trans here
        // parse senz first
        val senz = SenzParser.parseSenz(senzMsg)
        senz match {
          case Senz(SenzType.PUT, _, _, attr, _) =>
            if (attr.contains("#block") && attr.contains("#sign")) {
              // block sign request received
              // start actor to sign the block
              context.actorOf(BlockSigner.props) ! Sign(None, Option(senz.sender), Option(attr("#block")))
            }
          case Senz(SenzType.DATA, _, _, attr, _) =>
            if (attr.contains("#block") && attr.contains("#sign")) {
              // block signed response received
              blockCreator ! SignResp(None, Option(senz.sender), attr.get("#block"), attr("#sign").toBoolean)
            }
          case _ =>
            logger.debug(s"Not support message: $senzMsg")
        }
      }
    case _: ConnectionClosed =>
      logger.debug("ConnectionClosed")
      context.stop(self)
    case Msg(msg) =>
      // sign senz
      val senzSignature = RSAFactory.sign(msg.trim.replaceAll(" ", ""))
      val signedSenz = s"$msg $senzSignature"

      logger.info("Senz: " + msg)
      logger.info("Signed senz: " + signedSenz)

      connection ! Write(ByteString(s"$signedSenz;"))
  }

}


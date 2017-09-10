package com.score.zchain.protocol

object SenzType extends Enumeration {
  type SenzType = Value
  val SHARE, GET, PUT, DATA, STREAM, PING, TAK, TIK = Value
}

import com.score.zchain.protocol.SenzType._

case class Msg(data: String)

case class Ping()

case class SenzMsg(senz: Senz, data: String)

case class Senz(senzType: SenzType, sender: String, receiver: String, attributes: Map[String, String], signature: Option[String])


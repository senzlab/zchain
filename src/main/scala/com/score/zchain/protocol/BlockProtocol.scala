package com.score.zchain.protocol

import java.util.UUID

import com.datastax.driver.core.utils.UUIDs

case class Block(bankId: String, id: UUID = UUIDs.random, transactions: List[Transaction], signatures: List[Signature] = List(), timestamp: Long)


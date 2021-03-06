package com.score.zchain.protocol

import java.util.UUID

import com.datastax.driver.core.utils.UUIDs

case class Block(bankId: String,
                 id: UUID = UUIDs.random,
                 hash: String,
                 transactions: List[Transaction],
                 balances: List[Balance],
                 signatures: List[Signature] = List(),
                 timestamp: Long)


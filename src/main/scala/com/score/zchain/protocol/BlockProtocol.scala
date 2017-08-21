package com.score.zchain.protocol

import java.util.UUID

import com.datastax.driver.core.utils.UUIDs

case class Block(bankId: String,
                 id: UUID = UUIDs.random,
                 hash: String,
                 balance: Balance,
                 transactions: List[Transaction],
                 signatures: List[Signature] = List(),
                 timestamp: Long)


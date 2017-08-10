package com.score.zchain.protocol

import java.util.UUID

import com.datastax.driver.core.utils.UUIDs

case class Transaction(bankId: String, id: UUID = UUIDs.random, from: String, to: String, amount: Int, timestamp: Long)


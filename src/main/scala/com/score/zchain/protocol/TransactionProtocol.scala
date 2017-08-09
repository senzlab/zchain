package com.score.zchain.protocol

case class Transaction(bankId: String, id: Int, from: String, to: String, amount: Int, timestamp: Long)


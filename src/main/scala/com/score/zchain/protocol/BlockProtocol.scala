package com.score.zchain.protocol

case class Block(bankId: String, id: Int, transactions: List[Transaction], signatures: List[Signature], timestamp: Long)


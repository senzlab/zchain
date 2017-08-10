package com.score.zchain.comp

import java.util.UUID

import com.score.zchain.protocol.{Block, Signature, Transaction}


trait ChainDbComp {

  val chainDb: ChainDb

  trait ChainDb {
    def createTransaction(transaction: Transaction)

    def getTransaction(bankId: String, id: UUID): Option[Transaction]

    def getTransactions: List[Transaction]

    def deleteTransactions(transactions: List[Transaction])

    def createBlock(block: Block)

    def getBlock(bankId: String, id: UUID): Option[Block]

    def getBlocks: List[Block]

    def updateBlockSignature(block: Block, signature: Signature)
  }

}


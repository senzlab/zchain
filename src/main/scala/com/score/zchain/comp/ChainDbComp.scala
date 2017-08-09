package com.score.zchain.comp

import com.score.zchain.protocol.{Block, Signature, Transaction}


trait ChainDbComp {

  val chainDb: ChainDb

  trait ChainDb {
    def createTransaction(transaction: Transaction)

    def getTransaction(bankId: String, id: Int): Option[Transaction]

    def getTransactions: List[Transaction]

    def deleteTransactions()

    def createBlock(block: Block)

    def getBlock(bankId: String, id: Int): Option[Block]

    def getBlocks: List[Block]

    def updateBlockSignature(block: Block, signature: Signature)
  }

}


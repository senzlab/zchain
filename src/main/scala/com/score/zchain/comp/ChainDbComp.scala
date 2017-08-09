package com.score.zchain.comp

import com.score.zchain.protocol.Transaction


trait ChainDbComp {

  val chainDb: ChainDb

  trait ChainDb {

    def createTransaction(transaction: Transaction)

    def getTransaction(bankId: String, id: Int): Option[Transaction]

    def getTransactions: List[Transaction]

    def deleteTransactions()

    def createBlock()

    def getBlock()

  }

}


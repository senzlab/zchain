package com.score.zchain.comp

import com.datastax.driver.core.querybuilder.QueryBuilder
import com.datastax.driver.core.querybuilder.QueryBuilder._
import com.score.zchain.protocol.{Block, Transaction}

import scala.collection.JavaConverters._

trait ChainDbCompImpl extends ChainDbComp {

  this: CassandraClusterComp =>

  val chainDb = new ChainDbImpl

  class ChainDbImpl extends ChainDb {
    def createTransaction(transaction: Transaction) = {
      // insert query
      val statement = QueryBuilder.insertInto("transactions")
        .value("bank_id", transaction.bankId)
        .value("id", transaction.id)
        .value("from", transaction.from)
        .value("to", transaction.to)
        .value("amount", transaction.amount)
        .value("timestamp", transaction.timestamp)

      session.execute(statement)
    }

    def getTransaction(bankId: String, id: Int): Option[Transaction] = {
      // select query
      val selectStmt = select().all()
        .from("transactions")
        .where(QueryBuilder.eq("bank_id", bankId)).and(QueryBuilder.eq("id", id))
        .limit(1)

      val resultSet = session.execute(selectStmt)
      val row = resultSet.one()

      if (row != null) {
        Option(Transaction(
          bankId,
          id,
          row.getString("from"),
          row.getString("to"),
          row.getInt("amount"),
          row.getLong("timestamp")
        ))
      }
      else None
    }

    def getTransactions(): List[Transaction] = {
      // select query
      val selectStmt = select().all()
        .from("transactions")

      // get all transactions
      val resultSet = session.execute(selectStmt)
      resultSet.all().asScala.map { row =>
        Transaction(
          row.getString("bank_id"),
          row.getInt("id"),
          row.getString("from"),
          row.getString("to"),
          row.getInt("amount"),
          row.getLong("timestamp")
        )
      }.toList
    }

    def deleteTransactions(): Unit = ???

    def createBlock(block: Block): Unit = ???

    def getBlock(bankId: String, id: Int): Option[Block] = ???

    def getBlocks(): List[Block] = ???
  }

}

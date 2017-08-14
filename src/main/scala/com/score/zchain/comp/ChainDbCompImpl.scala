package com.score.zchain.comp

import java.util.UUID

import com.datastax.driver.core.UDTValue
import com.datastax.driver.core.querybuilder.QueryBuilder
import com.datastax.driver.core.querybuilder.QueryBuilder._
import com.score.zchain.protocol.{Block, Signature, Transaction}
import com.score.zchain.util.DbFactory

import scala.collection.JavaConverters._

trait ChainDbCompImpl extends ChainDbComp {

  val chainDb = new ChainDbImpl

  class ChainDbImpl extends ChainDb {
    def createTransaction(transaction: Transaction) = {
      // insert query
      val statement = QueryBuilder.insertInto("transactions")
        .value("bank_id", transaction.bankId)
        .value("id", transaction.id)
        .value("from_acc", transaction.from)
        .value("to_acc", transaction.to)
        .value("amount", transaction.amount)
        .value("timestamp", transaction.timestamp)

      DbFactory.session.execute(statement)
    }

    def getTransaction(bankId: String, id: UUID): Option[Transaction] = {
      // select query
      val selectStmt = select()
        .all()
        .from("transactions")
        .where(QueryBuilder.eq("bank_id", bankId)).and(QueryBuilder.eq("id", id))
        .limit(1)

      val resultSet = DbFactory.session.execute(selectStmt)
      val row = resultSet.one()

      if (row != null) Option(Transaction(bankId, id, row.getString("from"), row.getString("to"), row.getInt("amount"), row.getLong("timestamp")))
      else None
    }

    def getTransactions: List[Transaction] = {
      // select query
      val selectStmt = select()
        .all()
        .from("transactions")

      // get all transactions
      val resultSet = DbFactory.session.execute(selectStmt)
      resultSet.all().asScala.map { row =>
        Transaction(row.getString("bank_id"), row.getUUID("id"), row.getString("from_acc"), row.getString("to_acc"), row.getInt("amount"), row.getLong("timestamp"))
      }.toList
    }

    def deleteTransactions(transactions: List[Transaction]) = {
      for (t <- transactions) {
        // delete query
        val delStmt = delete()
          .from("transactions")
          .where(QueryBuilder.eq("bank_id", t.bankId)).and(QueryBuilder.eq("id", t.id))

        DbFactory.session.execute(delStmt)
      }
    }

    def createBlock(block: Block) = {
      // UDT's
      val transType = DbFactory.cluster.getMetadata.getKeyspace("zchain").getUserType("transaction")

      // transactions
      val trans = block.transactions.map(t =>
        transType.newValue
          .setString("bank_id", t.bankId)
          .setUUID("id", t.id)
          .setString("from_acc", t.from)
          .setString("to_acc", t.to)
          .setInt("amount", t.amount)
          .setLong("timestamp", t.timestamp)
      ).asJava

      // insert query
      val statement = QueryBuilder.insertInto("blocks")
        .value("bank_id", block.bankId)
        .value("id", block.id)
        .value("hash", block.hash)
        .value("transactions", trans)
        .value("timestamp", block.timestamp)

      DbFactory.session.execute(statement)
    }

    def getBlock(bankId: String, id: UUID): Option[Block] = {
      // select query
      val selectStmt = select()
        .all()
        .from("blocks")
        .where(QueryBuilder.eq("bank_id", bankId)).and(QueryBuilder.eq("id", id))
        .limit(1)

      val resultSet = DbFactory.session.execute(selectStmt)
      val row = resultSet.one()

      if (row != null) {
        // get transactions
        val trans = row.getSet("transactions", classOf[UDTValue]).asScala.map(t =>
          Transaction(t.getString("bank_id"), t.getUUID("id"), t.getString("from_acc"), t.getString("to_acc"), t.getInt("amount"), t.getLong("timestamp"))
        ).toList

        // get signatures
        val sigs = row.getSet("signatures", classOf[UDTValue]).asScala.map(s =>
          Signature(s.getString("bank_id"), s.getString("signature"))
        ).toList

        // create block
        Option(Block(bankId, id, row.getString("hash"), trans, sigs, row.getLong("timestamp")))
      }
      else None
    }

    def getBlocks: List[Block] = {
      // select query
      val selectStmt = select()
        .all()
        .from("blocks")

      // get all transactions
      val resultSet = DbFactory.session.execute(selectStmt)
      resultSet.all().asScala.map { row =>
        // get transactions
        val trans = row.getSet("transactions", classOf[UDTValue]).asScala.map(t =>
          Transaction(t.getString("bank_id"), t.getUUID("id"), t.getString("from_acc"), t.getString("to_acc"), t.getInt("amount"), t.getLong("timestamp"))
        ).toList

        // get signatures
        val sigs = row.getSet("signatures", classOf[UDTValue]).asScala.map(s =>
          Signature(s.getString("bank_id"), s.getString("signature"))
        ).toList

        // create block
        Block(row.getString("bank_id"), row.getUUID("id"), row.getString("hash"), trans, sigs, row.getLong("timestamp"))
      }.toList
    }

    def updateBlockSignature(block: Block, signature: Signature) = {
      // signature type
      val sigType = DbFactory.cluster.getMetadata.getKeyspace("zchain").getUserType("signature")

      // signature
      val sig = sigType.newValue.setString("bank_id", signature.bankId).setString("digsig", signature.digsig)

      // existing signatures + new signature
      val sigs = block.signatures.map(s =>
        sigType.newValue
          .setString("bank_id", s.bankId)
          .setString("id", s.digsig)
      ) :+ sig

      // update query
      val statement = QueryBuilder.update("blocks")
        .`with`(QueryBuilder.add("signatures", sig))
        .where(QueryBuilder.eq("bank_id", block.bankId)).and(QueryBuilder.eq("id", block.id))

      DbFactory.session.execute(statement)
    }
  }

}

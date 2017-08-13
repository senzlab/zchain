package com.score.zchain.util

import com.score.zchain.comp.CassandraClusterComp
import com.score.zchain.config.SchemaConf

object DbFactory extends CassandraClusterComp with SchemaConf {
  val initDb = () => {
    // TODO we disabled this
    // session.execute(schemaCreateKeyspace)

    // create UDT
    session.execute(schemaCreateTypeTransaction)
    session.execute(schemaCreateTypeSignature)

    // create tables
    session.execute(schemaCreateTableTransactions)
    session.execute(schemaCreateTableBlocks)
  }
}

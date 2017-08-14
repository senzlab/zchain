package com.score.zchain.comp

import com.datastax.driver.core.{Cluster, HostDistance, PoolingOptions, Session}
import com.score.zchain.config.DbConf

/**
  * Cassandra database related configuration, we wrapped them with
  * trait in order to have self typed annotated dependencies
  *
  * @author eranga herath(erangaeb@gmail.com)
  */
object CassandraClusterComp extends DbConf {
//  lazy val poolingOptions: PoolingOptions = {
//    new PoolingOptions()
//      .setConnectionsPerHost(HostDistance.LOCAL, 4, 10)
//      .setConnectionsPerHost(HostDistance.REMOTE, 2, 4)
//  }
//
//  lazy val cluster: Cluster = {
//    Cluster.builder()
//      .addContactPoint(cassandraHost)
//      .withPoolingOptions(poolingOptions)
//      .build()
//  }
//
//  lazy val session: Session = cluster.connect(cassandraKeyspace)
}
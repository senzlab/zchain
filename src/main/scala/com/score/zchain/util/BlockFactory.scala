package com.score.zchain.util

import com.score.zchain.protocol.Transaction

import scala.annotation.tailrec

object BlockFactory {

  def markleHash(transactions: List[Transaction]) = {
    @tailrec
    def markle(ins: List[String], outs: List[String]): String = {
      ins match {
        case Nil =>
          // empty list
          if (outs.size == 1) outs.head
          else markle(outs, List())
        case x :: Nil =>
          // one element list
          markle(Nil, outs :+ RSAFactory.sha256(x + x))
        case x :: y :: l =>
          // have at least two elements in list
          // concat them and sign them
          markle(l, outs :+ RSAFactory.sha256(x + y))
      }
    }

    markle(transactions.map(t => RSAFactory.sha256(t.id.toString)), List())
  }

}

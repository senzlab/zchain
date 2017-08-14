package com.score.zchain.util

import com.score.zchain.protocol.Transaction

import scala.annotation.tailrec

object BlockFactory {

  def hash(blocks: List[Int]) = {
    def itr(lst: List[Int], nLst: List[Int]): Int = {
      lst match {
        case Nil =>
          // empty list
          if (nLst.size == 1) nLst.head
          else itr(nLst, List())
        case x :: Nil =>
          // one element list
          itr(Nil, nLst :+ x)
        case x :: y :: l =>
          // have at least two elements in list
          println(s"$x, $y --> $l --> ${nLst :+ (x + y)}")
          itr(l, nLst :+ (x + y))
      }
    }

    itr(blocks, List())
  }

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

  //  val t1 = Transaction("sampath", UUIDs.random(), "323", "2323", 34, 523232)
  //  val t2 = Transaction("sampath", UUIDs.random(), "323dd", "222323", 34, 523232)
  //  val t3 = Transaction("sampath", UUIDs.random(), "dd", "22232", 34, 523232)
  //  println(markleHash(Block("32", UUIDs.random, List(t1, t2, t3), List(), 23232)))
}

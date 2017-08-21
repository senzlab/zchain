package com.score.zchain

import com.score.zchain.actor.BlockCreator.Bal

import scalaz.Scalaz._

object Main extends App {

  // first
  //  1. setup logging
  //  2. setup keys
  //  3. setup db
  //SenzFactory.setupLogging()
  //SenzFactory.setupKeys()
  //DbFactory.initDb()

  // start block creator
  //implicit val system = ActorSystem("senz")
  //system.actorOf(BlockCreator.props, name = "BlockCreator") ! Create

  case class Trans(from: String, to: String, amount: Int)

  val tl = List(
    Trans("a", "b", 30),
    Trans("a", "c", 40),
    Trans("b", "c", 10),
    Trans("b", "a", 25),
    Trans("c", "a", 15)
  )

  val m = tl.foldMap {
    case Trans(from, to, am) =>
      Map(from -> (am, 0), to -> (0, am))
  }.map {
    case (name, (in, out)) =>
      Bal(name, in, out)
  }

  println(m)

}

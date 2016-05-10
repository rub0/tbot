package com.telegram.bot

import akka.actor.Actor
import akka.actor.Actor.Receive

/**
  * Created by rmulero on 10/05/16.
  */
class StupidBotActor extends Actor{
  override def receive: Receive = {
    case _ => None
  }
}

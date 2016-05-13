package com.telegram.bot

import akka.actor.Actor
import akka.actor.Actor.Receive

/**
  * Created by rmulero on 10/05/16.
  */
class StupidBotActor extends Actor{
  override def receive: Receive = {
    case BotMessage(chatId, args: Seq[String]) =>
      StupidBot.sendMessage(chatId, "i have just received a new message :)")
    case _ => None
  }
}

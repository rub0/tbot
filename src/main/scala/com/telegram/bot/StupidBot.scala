package com.telegram.bot

import akka.actor.{Props, ActorSystem, ActorRef}
import akka.stream.ActorMaterializer
import com.telegram.api.{Update, TelegramBot}
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import scala.concurrent.Future

case class BotMessage(chatId: Int, message: Seq[String])

/**
  * Created by rmulero on 10/05/16.
  */
object StupidBot extends TelegramBot("ficheroAleer") with Runnable{

  implicit def toOption[T](x:T) : Option[T] = Option(x)

  implicit val system = ActorSystem("rest")
  implicit val materializer = ActorMaterializer()

  val cmdActor: ActorRef = system.actorOf(Props(new StupidBotActor))

  val pollingCycle = 1000
  private var running = true

  override def run(): Unit = {
    var updatesOffset = 0
    while (running) {
      for (updates <- getUpdates(offset = updatesOffset)) {
        for (u <- updates ) {
          Future(handleUpdate(u))
          updatesOffset = updatesOffset max (u.updateId + 1)
        }
      }
      Thread.sleep(pollingCycle)
    }
  }

  def setup(): Unit = {

  }

  /**
    * handleUpdate
    *
    * Process incoming updates (comming from polling, webhooks...)
    */
  override def handleUpdate(update: Update): Unit = {
    for {
      msg <- update.message
      text <- msg.text
    } /* do */ {

      println("Message: " + text)

      // TODO: Allow parameters with spaces e.g. /echo "Hello World"
      val tokens = text.trim split " "
      tokens match {
        case Array(rawCmd, args @ _*) if rawCmd startsWith cmdPrefix =>

          val parts = rawCmd stripPrefix cmdPrefix split cmdAt
          val cmd = parts.head.toLowerCase
          val addressees = parts.tail map (_.toLowerCase)

          if (addressees.isEmpty || addressees.contains(botName.toLowerCase)) {
            cmdActor ! BotMessage(msg.chat.id, args)
          }

        case _ => sendMessage(msg.chat.id, text)
      }
    }

  }

}

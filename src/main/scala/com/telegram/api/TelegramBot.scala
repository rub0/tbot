package com.telegram.api

import com.telegram.http.{TelegramHttpClient, HttpClient}

import scala.concurrent.{ExecutionContext, Await}
import scala.concurrent.duration._
import ExecutionContext.Implicits.global


/**
  * Created by rmulero on 10/05/16.
  */
abstract class TelegramBot(val token: String) extends TelegramAPI(token) with TelegramHttpClient {
  lazy val botName: String = Await.result(getMe.map(_.username.get), 5 seconds)

  val cmdPrefix = "/"

  // Allows targeting specific bots eg. /hello@FlunkeyBot
  val cmdAt = "@"

  /**
    * handleUpdate
    *
    * Process incoming updates (comming from polling, webhooks...)
    */
  def handleUpdate(update: Update): Unit

}

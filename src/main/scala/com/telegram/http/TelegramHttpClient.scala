package com.telegram.http

import com.telegram.api.InputFile
import scalaj.http.{Http, MultiPart}

import scala.concurrent.{ExecutionContext, Promise, Future}
import scala.util.Try
import scalaj.http.MultiPart
import ExecutionContext.Implicits.global


/**
  * Created by rmulero on 10/05/16.
  */
trait TelegramHttpClient extends HttpClient{
  val ConnectionTimeout = 2000
  val ReadTimeout = 10000

  def request(requestUrl: String, params : (String, Any)*): String = {
    // TODO: Set appropriate timeout values
    val query = params.foldLeft(Http(requestUrl).timeout(ConnectionTimeout, ReadTimeout)) {
      case (q, (id, value)) => value match {
        case file: InputFile =>
          // TODO: Get the corret MIME type, right now the server ignored it or does some content-based MIME detection
          q.postMulti(MultiPart(id, file.name, file.mimeType, file.bytes))

        case Some(s) =>
          q.param(id, s.toString)

        case None => q

        case _ => q.param(id, value.toString)
      }
    }

    val response = query.asString
    if (response.isSuccess)
      response.body
    else
      throw new Exception("HTTP request error " + response.code + ": " + response.statusLine)
  }

  def asyncRequest(requestUrl: String, params : (String, Any)*): Future[String] = {
    val p = Promise[String]()
    Future {
      p.complete(Try(request(requestUrl, params: _*)))
    }
    p.future
  }
}

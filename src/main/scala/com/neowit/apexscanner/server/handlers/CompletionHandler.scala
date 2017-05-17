/*
 * Copyright (c) 2017 Andrey Gavrikov.
 *
 * This file is part of https://github.com/neowit/apexscanner
 *
 *  This file is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  This file is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 */

package com.neowit.apexscanner.server.handlers

import com.neowit.apexscanner.server.protocol.LanguageServer
import com.neowit.apexscanner.server.protocol.messages.MessageParams.CompletionParams
import com.neowit.apexscanner.server.protocol.messages._

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by Andrey Gavrikov 
  */
class CompletionHandler extends MessageHandler with MessageJsonSupport {
    override protected def handleImpl(server: LanguageServer, messageIn: RequestMessage)(implicit ex: ExecutionContext): Future[Either[ResponseError, ResponseMessage]] = {
        val result =
        messageIn.params match {
          case Some(json) =>
              json.as[CompletionParams]  match {
                  case Right(params) =>
                      //server.initialiseProject(params)
                      //val serverCapabilities = ServerCapabilities()
                      //Right(ResponseMessage(messageIn.id, result = Option(Map("capabilities" -> serverCapabilities.asJson).asJson), error = None))
                      println(params)
                      ???
                  case Left(err) =>
                      Left(ResponseError(ErrorCodes.InvalidParams, s"Failed to parse message: $messageIn. Error: $err"))
              }
          case None =>
              Left(ResponseError(ErrorCodes.InvalidParams, s"Failed to parse message: $messageIn. Missing params."))
        }
        Future.successful(result)
    }
}

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

package com.neowit.apexscanner.server.protocol

import com.neowit.apexscanner.server.handlers.InitializeHandler
import com.neowit.apexscanner.server.protocol.messages._

/**
  * Created by Andrey Gavrikov 
  */
trait LanguageServer {

    //private val msgReader = new MessageReader(inStream)
    //private val msgWriter = new MessageWriter(outStream)

    def shutdown(): Unit

    def process(message: Message): Option[ResponseMessage] = {
        message match {
            case m @ RequestMessage(id, "initialize", initializeParams) =>
                val handler = new InitializeHandler()
                val msg = handler.handle(m)
                Option(msg)
            case RequestMessage(_, "shutdown", None) =>
                shutdown()
                None
            case NotificationMessage("cancelRequest", _) =>
                //A processed notification message must not send a response back. They work like events.
                //TODO
                None

            case NotificationMessage(_, _) =>
                //A processed notification message must not send a response back. They work like events.
                //TODO
                None
            case _ =>
                val error = ResponseError(ErrorCodes.MethodNotFound, s"Message not supported: $message")
                val msg = ResponseMessage(id = 0, result = None, error = Option(error))
                Option(msg)

        }
    }

}

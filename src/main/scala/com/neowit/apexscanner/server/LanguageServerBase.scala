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

package com.neowit.apexscanner.server

import java.io.{InputStream, OutputStream}

import com.neowit.apexscanner.server.protocol.LanguageServer
import com.neowit.apexscanner.server.protocol.messages.NotificationMessage
import com.typesafe.scalalogging.LazyLogging

/**
  * Created by Andrey Gavrikov 
  */
abstract class LanguageServerBase(inputStream: InputStream, outputStream: OutputStream) extends LanguageServer with LazyLogging {

    private val reader = new MessageReader(inputStream)
    private val writer = new MessageWriter(outputStream)

    protected def isConnectionOpen: Boolean = !reader.isStreamClosed

    override def sendNotification(notification: NotificationMessage): Unit = {
        writer.write(notification)
    }


    def start(): Unit = {
        while (isConnectionOpen) {
            reader.read().foreach{message =>
                //logger.debug("Received:")
                //logger.debug(message.toString)

                process(message) map {
                    case Right(response) =>
                        writer.write(response)
                    case Left(_) =>
                }

            }
        }
        shutdown()
    }
    override def shutdown(): Unit = {
        logger.debug("SHUTDOWN...")
        sys.exit(0)
    }
}

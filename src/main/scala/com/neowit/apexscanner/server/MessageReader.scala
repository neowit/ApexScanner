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

import java.io.{BufferedReader, InputStream, InputStreamReader}

import com.neowit.apexscanner.server.protocol.{ContentLengthHeader, ContentTypeHeader, MessageHeader}
import com.neowit.apexscanner.server.protocol.messages.{Message, MessageJsonSupport, NotificationMessage, RequestMessage}
import com.typesafe.scalalogging.LazyLogging
import io.circe.parser._
/**
  * Created by Andrey Gavrikov 
  */
class MessageReader (in: InputStream) extends MessageJsonSupport with LazyLogging {
    private val reader = new BufferedReader(new InputStreamReader(in))
    private var isClosed = false
    def isStreamClosed: Boolean = isClosed

    private def readRaw(): String = {
        val headerStr = reader.readLine()
        logger.debug(headerStr)
        if (null == headerStr) {
            isClosed = true
            logger.debug("Looks like Input Stream is closed")
            headerStr
        } else {
            MessageHeader.parse(headerStr) match {
                case Some(ContentLengthHeader(len)) =>
                    // have to skip 2 bytes because of additional '\r\n'
                    // see: https://github.com/Microsoft/language-server-protocol/blob/master/protocol.md#header-part
                    reader.skip(2)
                    // read the rest of the message
                    val data = new Array[Char](len)
                    reader.read(data)
                    logger.debug(data.mkString)
                    data.mkString
                case Some(ContentTypeHeader(contentType)) =>
                    logger.debug(headerStr)
                    ???
                case _ =>
                    logger.debug("fallback header route: " + headerStr)
                    headerStr

            }
        }
    }

    def read(): Seq[Message] = {
        val jsonStr = readRaw()
        if (isClosed) {
            isClosed = true
            Seq.empty
        } else {
            parse(jsonStr) match {
                case Left(failure) =>
                    logger.error(failure.message)
                    Seq.empty
                case Right(json) =>

                    val decoderResult =
                        if (json.asObject.exists(_.fieldSet.contains("id"))) {
                            json.as[RequestMessage]
                        } else {
                            json.as[NotificationMessage]
                        }

                    decoderResult match {
                        case Right(msg) => Seq(msg)
                        case Left(failure)  =>
                            logger.error("Failed to parse request")
                            logger.error(jsonStr)
                            logger.error(failure.message)
                            Seq.empty
                    }
            }
        }
    }
}


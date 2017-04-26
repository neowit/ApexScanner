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
import com.neowit.apexscanner.server.protocol.messages.{Message, MessageJsonSupport, RequestMessage}
import io.circe.parser._
/**
  * Created by Andrey Gavrikov 
  */
class MessageReader (in: InputStream) extends MessageJsonSupport {
    private val reader = new BufferedReader(new InputStreamReader(in))

    private def readRaw(): String = {
        val headerStr = reader.readLine()
        MessageHeader.parse(headerStr) match {
            case Some(ContentLengthHeader(len)) =>
                // have to skip 2 bytes because of additional '\r\n'
                // see: https://github.com/Microsoft/language-server-protocol/blob/master/protocol.md#header-part
                reader.skip(2)
                // read the rest of the message
                val data = new Array[Char](len)
                reader.read(data)
                data.mkString
            case Some(ContentTypeHeader(contentType)) => ???
            case _ => ???
        }
    }

    def read(): Seq[Message] = {
        val jsonStr = readRaw()
        parse(jsonStr) match {
            case Left(failure) =>
                println(failure.message)
                Seq.empty
            case Right(json) =>
                json.as[RequestMessage] match {
                    case Right(msg) => Seq(msg)
                    case Left(failure)  =>
                        println(failure.message)
                        Seq.empty
                }
                /*
                json.asObject match {
                  case Some(jsonObject) =>
                      val valueMap = jsonObject.toMap
                      println(valueMap)
                  case None =>
                }
                */
        }
    }
}


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

import java.io.OutputStream
import java.nio.charset.Charset

import com.neowit.apexscanner.server.protocol.{ContentLengthHeader, MessageHeader}
import io.circe.syntax._
import io.circe.{Json, Printer}
import com.neowit.apexscanner.server.protocol.messages.{MessageJsonSupport, NotificationMessage, ResponseMessage}
import com.typesafe.scalalogging.LazyLogging

/**
  * Created by Andrey Gavrikov 
  */
class MessageWriter(out: OutputStream) extends MessageJsonSupport with LazyLogging {
    import MessageWriter._

    // protect output stream from concurrent writes by multiple threads
    private val lock = new Object
    /**
      * This method can be called from multiple threads so have to block to finish writing.
      */
    def write(msg: ResponseMessage): Unit = lock.synchronized {
        write(msg.asJson)
    }
    def write(notification: NotificationMessage): Unit = {
        write(notification.asJson)
    }
    private def write(msg: Json): Unit = lock.synchronized {
        val jsonString = DROP_NULLS_PRINTER.print( msg )
        val payloadBytes = jsonString.getBytes(MessageWriter.Utf8Charset)
        // write header
        writeHeader(ContentLengthHeader(payloadBytes.length))
        // write main payload (it must be separated from headers by extra "\n\r" sequence)
        out.write("\r\n".getBytes(AsciiCharset))
        out.write(payloadBytes)
        out.flush()
        logger.debug(jsonString)
    }
    private def writeHeader(header: MessageHeader): Unit = {
        logger.debug(header.toString + "\r\n")
        out.write(header.toString.getBytes(AsciiCharset))
        out.write("\r\n".getBytes(AsciiCharset))
    }
}
object MessageWriter {
    val AsciiCharset: Charset = Charset.forName("ASCII")
    val Utf8Charset: Charset = Charset.forName("UTF-8")

    // when serialising JSON omit None values.
    // By default it results in "key": null which is ugly
    // https://github.com/circe/circe/issues/585
    val DROP_NULLS_PRINTER: Printer = Printer.noSpaces.copy(dropNullValues = true)
}

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

/**
  * Created by Andrey Gavrikov 
  */
sealed trait HeaderType

case object ContentLength extends HeaderType
case object ContentType extends HeaderType

//Content-Length: 1167
trait MessageHeader {
    val headerType: HeaderType
}

case class ContentLengthHeader(length: Int) extends MessageHeader {
    override val headerType: HeaderType = ContentLength

    override def toString: String = MessageHeader.CONTENT_LENGTH_PREFIX + length
}
case class ContentTypeHeader(contentType: String) extends MessageHeader {
    override val headerType: HeaderType = ContentType
}

object MessageHeader {
    val CONTENT_LENGTH_PREFIX = "Content-Length: "
    val CONTENT_TYPE_PREFIX = "Content-Type: "
    def parse(headerRaw: String): Option[MessageHeader] = {
        extractHeaderValue(headerRaw, CONTENT_LENGTH_PREFIX) match {
            case Some(lengthStr) => Option(ContentLengthHeader(lengthStr.toInt))
            case None =>
                extractHeaderValue(headerRaw, CONTENT_TYPE_PREFIX) match {
                    case Some(contentType) => Option(ContentTypeHeader(contentType))
                    case None => None
                }
        }
    }

    def extractHeaderValue(headerRaw: String, prefix: String): Option[String] = {
        if (null == headerRaw) {
            None
        } else if (headerRaw.startsWith(prefix)) {
            val valueStr = headerRaw.substring(prefix.length)
            if (valueStr.nonEmpty) {
                Option(valueStr)
            } else {
                None
            }
        } else {
            None
        }
    }
}

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

package com.neowit.apexscanner

import java.io.{ByteArrayInputStream, InputStream}
import java.nio.charset.StandardCharsets
import java.nio.file.Path

import com.neowit.apexscanner.VirtualDocument.DocumentId
import com.neowit.apexscanner.nodes.Position
import org.antlr.v4.runtime.{CharStream, CharStreams}

/**
  * Created by Andrey Gavrikov 
  */
case class TextBasedDocument (text: String, fileOpt: Option[Path], offset: Option[Position]) extends VirtualDocument {
    override def inputStream: InputStream = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8))

    override def getTextContent: Option[String] = Option(text)

    override def getCharStream: CharStream = {
        CharStreams.fromString(this.text)
    }

    override def getId: DocumentId = {
        fileOpt match {
            case Some(_) => super.getId
            case None => text.hashCode.toString
        }
    }
}

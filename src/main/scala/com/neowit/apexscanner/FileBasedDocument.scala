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

import java.io._
import java.nio.charset.StandardCharsets
import java.nio.file.Path

import com.neowit.apexscanner.nodes.Position
import org.antlr.v4.runtime.{CharStream, CharStreams}

import scala.util.Try

/**
  * Created by Andrey Gavrikov 
  */
case class FileBasedDocument(file: Path) extends VirtualDocument {

    override def fileOpt: Option[Path] = Option(file)

    override def inputStream: InputStream = {
        new FileInputStream(file.toFile)
    }

    override def getTextContent: Option[String] = {
        Try{
            val source = scala.io.Source.fromInputStream(this.inputStream, StandardCharsets.UTF_8.name())
            source.getLines().mkString("\\n")
        }.toOption
    }

    override def getCharStream: CharStream = {
        CharStreams.fromStream(this.inputStream, StandardCharsets.UTF_8)
    }

    // it is highly unlikely that a document based on a concrete file will have parent (outer) document
    override def offset: Option[Position] = None
}

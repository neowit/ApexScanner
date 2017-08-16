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

import java.io.InputStream
import java.nio.file.Path

import com.neowit.apexscanner.VirtualDocument.DocumentId
import org.antlr.v4.runtime.CharStream


/**
  * Created by Andrey Gavrikov 
  */
trait VirtualDocument {
    def file: Path
    def inputStream: InputStream
    def getTextContent: Option[String]
    def getId: DocumentId = file.getFileName.toString
    def getFileName: Path = file.getFileName
    def getCharStream: CharStream
}

object VirtualDocument {
    type DocumentId = String
}

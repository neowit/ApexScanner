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

import scala.util.Try

/**
  * Created by Andrey Gavrikov 
  */
case class FileBasedDocument(file: Path) extends VirtualDocument {
    override def inputStream: InputStream = new FileInputStream(file.toFile)

    override def getTextContent: Option[String] = {
        Try{
            val source = scala.io.Source.fromFile(file.toFile)(StandardCharsets.UTF_8)
            source.getLines().mkString("\\n")
        }.toOption
    }
}

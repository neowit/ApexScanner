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

import java.net.URI
import java.nio.file.{Path, Paths}

import com.typesafe.scalalogging.LazyLogging

import scala.util.{Failure, Success, Try}

/**
  * Created by Andrey Gavrikov 
  */
case class DocumentUri(uri: String) {

    val path: Option[Path] = DocumentUri.uriToPath(uri)
}

object DocumentUri extends LazyLogging {
    def apply(file: Path): DocumentUri = {
        DocumentUri(file.toUri.toASCIIString)
    }

    def uriToPath(uri: String): Option[Path] = {
        Try{
            Paths.get(URI.create(uri))
        } match {
            case Success(_path) => Option(_path)
            case Failure(ex) =>
                logger.error(s"URI to Path conversion failed for: $uri. " + ex)
                None
        }
    }
}

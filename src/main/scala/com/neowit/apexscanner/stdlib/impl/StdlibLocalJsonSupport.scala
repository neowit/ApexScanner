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

package com.neowit.apexscanner.stdlib.impl

import com.neowit.apexscanner.stdlib._
import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

/**
  * Created by Andrey Gavrikov 
  */
trait StdlibLocalJsonSupport {
    implicit val StdlibPropertyDecoder: Decoder[StdlibProperty] = deriveDecoder
    implicit val StdlibMethodParameterDecoder: Decoder[StdlibMethodParameter] = deriveDecoder
    implicit val StdlibMethodDecoder: Decoder[StdlibMethod] = deriveDecoder
    implicit val StdlibClassDecoder: Decoder[StdlibClass] = deriveDecoder
    //implicit val StdlibNamespaceDecoder: Decoder[StdlibNamespace] = deriveDecoder
    implicit val StdlibNamespaceDecoder: Decoder[StdlibNamespace] = Decoder.decodeJsonObject.emap { jsonObj =>
        val classByName =
        jsonObj.toMap.map{
            case (className, classJson) =>
                className -> classJson.as[StdlibClass].toOption
        }.filter{
            case (className, Some(stdlibClass)) => true
            case _ => false
        }.collect{
            case (className, Some(stdlibClass)) => className -> stdlibClass
        }
        if (classByName.isEmpty) {
            Left("Failed to parse StdlibNamespace")
        } else {
            Right(StdlibNamespace(classMap = classByName))
        }
        //Either.catchNonFatal(Instant.parse(str)).leftMap(t => "Instant")
    }
    implicit val ApexAPIDecoder: Decoder[ApexAPI] = deriveDecoder
}

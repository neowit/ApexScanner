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

package com.neowit.apexscanner.extlib.impl.stdlib

import com.typesafe.scalalogging.LazyLogging
import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

/**
  * Created by Andrey Gavrikov 
  */
trait StdlibLocalJsonSupport extends LazyLogging {
    implicit val ApexApiJsonPropertyDecoder: Decoder[ApexApiJsonProperty] = deriveDecoder
    implicit val ApexApiJsonMethodParameterDecoder: Decoder[ApexApiJsonMethodParameter] = deriveDecoder
    implicit val ApexApiJsonMethodDecoder: Decoder[ApexApiJsonMethod] = deriveDecoder
    implicit val ApexApiJsonClassDecoder: Decoder[ApexApiJsonClass] = deriveDecoder
    //implicit val ApexApiJsonNamespaceDecoder: Decoder[ApexApiJsonNamespace] = deriveDecoder
    implicit val ApexApiJsonNamespaceDecoder: Decoder[ApexApiJsonNamespace] = Decoder.decodeJsonObject.emap { jsonObj =>
        val classByName =
        jsonObj.toMap.map{
            case (className, classJson) =>
                className -> classJson.as[ApexApiJsonClass].toOption
        }.filter{
            case (className, Some(stdlibClass)) => true
            case (className, None) =>
                logger.error("Failed to parse: " + className)
                false
            case _ =>
                logger.error("Failed to parse unknown")
                false
        }.collect{
            case (className, Some(stdlibClass)) =>
                className -> stdlibClass
        }
        if (classByName.isEmpty) {
            Left("Failed to parse ApexApiJsonNamespace")
        } else {
            Right(ApexApiJsonNamespace(classMap = classByName))
        }
        //Either.catchNonFatal(Instant.parse(str)).leftMap(t => "Instant")
    }
    implicit val ApexApiDecoder: Decoder[ApexApiJson] = deriveDecoder
}

trait ApexApiJsonRuleNode


case class ApexApiJsonNamespace(classMap: Map[String, ApexApiJsonClass]) extends ApexApiJsonRuleNode


case class ApexApiJsonProperty(name: String) extends ApexApiJsonRuleNode
case class ApexApiJsonMethodParameter(name: String, `type`: String)
case class ApexApiJsonMethod(argTypes: Option[Array[String]],
                             isStatic: Option[Boolean],
                             methodDoc: Option[String],
                             name: String,
                             parameters: Array[ApexApiJsonMethodParameter],
                             returnType: Option[String]
                       ) extends ApexApiJsonRuleNode

case class ApexApiJsonClass(constructors: Array[ApexApiJsonMethod], methods: Array[ApexApiJsonMethod], properties: Array[ApexApiJsonProperty]) extends ApexApiJsonRuleNode

case class ApexApiJson(publicDeclarations: Map[String, ApexApiJsonNamespace]) extends ApexApiJsonRuleNode
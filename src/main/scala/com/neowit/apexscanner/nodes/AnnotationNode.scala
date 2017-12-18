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

package com.neowit.apexscanner.nodes

import com.neowit.apexscanner.antlr.ApexcodeParser.{AnnotationContext, AnnotationElementValuePairContext}

case class AnnotationNode(name: String, body: Option[Either[AnnotationValue, List[AnnotationParameter]]], range: Range) extends AstNode {
    override def nodeType: AstNodeType = AnnotationNodeType

    /**
      * used for debug purposes
      *
      * @return textual representation of this node and its children
      */
    override def getDebugInfo: String = {
        val myText =
            body match {
                case Some(Left(AnnotationValue(_value, _))) =>
                    name + "(" +  _value + ")"
                case Some(Right(_params @ List(AnnotationParameter(_name, _value, _)))) =>
                    name + "(" + _params.map(_.getDebugInfo).mkString(",") + ")"
                case _ => name

            }
        super.getDebugInfo + " " + myText
    }
}

object AnnotationNode {
    import scala.collection.JavaConverters._
    def visitAnnotation(ctx: AnnotationContext): AstNode = {
        val body: Option[Either[AnnotationValue, List[AnnotationParameter]]] =
            if (null != ctx.annotationElementValuePairs()) {
                val paramOps: List[Option[AnnotationParameter]] =
                    ctx.annotationElementValuePairs()
                        .annotationElementValuePair()
                        .asScala.map(pair => createAnnotationParameter(pair)).toList
                // remove empty values
                Option(Right(paramOps.filterNot(_.isEmpty).flatten))
            }else if (null != ctx.annotationElementValue()) {
                Option(Left(AnnotationValue(ctx.annotationElementValue().getText, Range(ctx.annotationElementValue()))))
            } else {
                None // empty annotation body
            }
        val annotation = AnnotationNode(name = ctx.annotationName().getText, body, Range(ctx))
        annotation
    }
    def createAnnotationParameter(valueContext: AnnotationElementValuePairContext): Option[AnnotationParameter] = {
        if (null != valueContext.Identifier()) {
            if (null != valueContext.annotationElementValue()) {
                Option(AnnotationParameter(valueContext.Identifier().getText, valueContext.annotationElementValue().getText, Range(valueContext)))
            } else {
                // TODO - is this ever being used ?
                Option(AnnotationParameter(valueContext.Identifier().getText, "", Range(valueContext)))
            }
        } else {
            None
        }
    }
}

trait AnnotationBody extends AstNode {
    override def nodeType: AstNodeType = AnnotationParameterNodeType
}
case class AnnotationValue(value: String, range: Range) extends AnnotationBody {
    override def nodeType: AstNodeType = AnnotationParameterNodeType
    override def getDebugInfo: String = {
        super.getDebugInfo + " " + value
    }
}
case class AnnotationParameter(name: String, value: String, range: Range) extends AnnotationBody {
    override def nodeType: AstNodeType = AnnotationParameterNodeType
    override def getDebugInfo: String = {
        super.getDebugInfo + " " + name + "=" + value
    }
}


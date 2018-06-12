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
import com.neowit.apexscanner.ast.QualifiedName
import com.neowit.apexscanner.symbols
import com.neowit.apexscanner.symbols.SymbolKind

case class AnnotationNode(override val name: Option[String], body: Option[Either[AnnotationValue, List[AnnotationParameter]]], range: Range)
                            extends ClassVariableNodeBase {
    override def nodeType: AstNodeType = AnnotationNodeType
    override def getValueType: Option[ValueType] = name.map(n => ValueTypeAnnotation(QualifiedName(n)))


    override def isScope: Boolean = true

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

    override def symbolName: String = name.getOrElse("")

    override def symbolKind: SymbolKind = SymbolKind.Annotation

    //override def symbolLocation: Location = LocationUndefined

    override def parentSymbol: Option[symbols.Symbol] = None

    override def symbolIsStatic: Boolean = false

    override def symbolValueType: Option[String] = Option("@" + symbolName)

    override def visibility: Option[String] = Option("") // annotations do not have visibility
}

object AnnotationNode {
    import scala.collection.JavaConverters._

    val ANNOTATIONS_NODE_NAME: String = "_Annotations"

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
        val annotation = AnnotationNode(name = Option(ctx.annotationName().getText), body, Range(ctx))
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

    // hardcoded list of Apex annotations for now because
    // not sure where list of annotations can be retrieved dynamically
    private val _stdAnnotations: Seq[AnnotationNode] =
        Seq(
            AnnotationNode(name = Option("AuraEnabled"), body = None, Range.INVALID_LOCATION)
            ,AnnotationNode(name = Option("Deprecated"), body = None, Range.INVALID_LOCATION)
            ,AnnotationNode(name = Option("Future"), body = None, Range.INVALID_LOCATION)
            ,AnnotationNode(name = Option("InvocableMethod"), body = None, Range.INVALID_LOCATION)
            ,AnnotationNode(name = Option("InvocableVariable"), body = None, Range.INVALID_LOCATION)
            ,AnnotationNode(name = Option("IsTest"), body = None, Range.INVALID_LOCATION)
            ,AnnotationNode(name = Option("ReadOnly"), body = None, Range.INVALID_LOCATION)
            ,AnnotationNode(name = Option("RemoteAction"), body = None, Range.INVALID_LOCATION)
            ,AnnotationNode(name = Option("SuppressWarnings"), body = None, Range.INVALID_LOCATION)
            ,AnnotationNode(name = Option("TestSetup"), body = None, Range.INVALID_LOCATION)
            ,AnnotationNode(name = Option("TestVisible"), body = None, Range.INVALID_LOCATION)
            ,AnnotationNode(name = Option("RestResource"), body = None, Range.INVALID_LOCATION)
            ,AnnotationNode(name = Option("HttpDelete"), body = None, Range.INVALID_LOCATION)
            ,AnnotationNode(name = Option("HttpGet"), body = None, Range.INVALID_LOCATION)
            ,AnnotationNode(name = Option("HttpPatch"), body = None, Range.INVALID_LOCATION)
            ,AnnotationNode(name = Option("HttpPost"), body = None, Range.INVALID_LOCATION)
            ,AnnotationNode(name = Option("HttpPut"), body = None, Range.INVALID_LOCATION)
            ,AnnotationNode(name = Option("namespaceAccessible"), body = None, Range.INVALID_LOCATION)
        )

    /**
      * @return complete list of all standard Apex annotations
      */
    def getStdAnnotations: Seq[AnnotationNode] = _stdAnnotations
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


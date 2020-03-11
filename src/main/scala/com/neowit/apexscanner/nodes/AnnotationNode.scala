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

case class AnnotationNode(override val name: Option[String], body: Option[AnnotationBody], range: Range)
                            extends ClassVariableNodeBase {
    override def nodeType: AstNodeType = AnnotationNodeType
    override def getValueType: Option[ValueType] = name.map(n => ValueTypeAnnotation(QualifiedName(n)))

    override def isMethodLike: Boolean = true
    override def methodParameters: Seq[String] = body.map(_.getParameters).getOrElse(Nil)

    override def isScope: Boolean = true

    /**
      * used for debug purposes
      *
      * @return textual representation of this node and its children
      */
    override def getDebugInfo: String = {
        val nameStr = name.getOrElse("")
        val myText =
            body match {
                case Some(AnnotationValue(_value, _)) =>
                    nameStr + "(" +  _value + ")"
                case Some(AnnotationParameterList(params, _)) =>
                    nameStr + "(" + params.map(_.getDebugInfo).mkString(",") + ")"
                case _ => nameStr

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

    override def symbolLabel: String = {
        val bodyStr = body match {
            case Some(_) => "(" + methodParameters.mkString(" ") + ")"
            case None => ""
        }
        "@" + symbolName + bodyStr
    }

    override def symbolInsertText: String = {
        val bodyStr = body match {
            case Some(_) => "(" + methodParameters.mkString(" ") + ")"
            case None => ""
        }
        symbolName + bodyStr
    }
}

object AnnotationNode {
    //import scala.collection.JavaConverters._
    import scala.jdk.CollectionConverters._

    val ANNOTATIONS_NODE_NAME: String = "_Annotations"

    def visitAnnotation(ctx: AnnotationContext): AstNode = {
        val body: Option[AnnotationBody] =
            if (null != ctx.annotationElementValuePairs()) {
                val paramOps: List[Option[AnnotationParameter]] =
                    ctx.annotationElementValuePairs()
                        .annotationElementValuePair()
                        .asScala.map(pair => createAnnotationParameter(pair)).toList
                // remove empty values
                Option(AnnotationParameterList(paramOps.filterNot(_.isEmpty).flatten))
            }else if (null != ctx.annotationElementValue()) {
                Option(AnnotationValue(ctx.annotationElementValue().getText, Range(ctx.annotationElementValue())))
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
            AnnotationNode(name = Option("AuraEnabled"),
                body = Option(AnnotationParameterList(("cacheable", "false", AnnotationParameterValueBoolean))), Range.INVALID_LOCATION)
            ,AnnotationNode(name = Option("Deprecated"), body = None, Range.INVALID_LOCATION)
            ,AnnotationNode(name = Option("Future"), body = None, Range.INVALID_LOCATION)
            ,AnnotationNode(name = Option("InvocableMethod"),
                body = Option(
                    AnnotationParameterList(List(AnnotationParameter("label", "<method label here>"),
                        AnnotationParameter("description", "<method description here>")))
                ),
                Range.INVALID_LOCATION)
            ,AnnotationNode(name = Option("InvocableVariable"),
                body = Option(
                    AnnotationParameterList(List(AnnotationParameter("label", "<variable label here>"),
                        AnnotationParameter("required", "true", AnnotationParameterValueBoolean)))
                ),
                Range.INVALID_LOCATION)
            ,AnnotationNode(name = Option("IsTest"),
                body = Option(AnnotationParameterList(("SeeAllData", "false", AnnotationParameterValueBoolean))), Range.INVALID_LOCATION)
            ,AnnotationNode(name = Option("IsTest"),
                body = Option(AnnotationParameterList(("isParallel", "true", AnnotationParameterValueBoolean))), Range.INVALID_LOCATION)
            ,AnnotationNode(name = Option("ReadOnly"), body = None, Range.INVALID_LOCATION)
            ,AnnotationNode(name = Option("RemoteAction"), body = None, Range.INVALID_LOCATION)
            ,AnnotationNode(name = Option("SuppressWarnings"), body = None, Range.INVALID_LOCATION)
            ,AnnotationNode(name = Option("TestSetup"), body = None, Range.INVALID_LOCATION)
            ,AnnotationNode(name = Option("TestVisible"), body = None, Range.INVALID_LOCATION)
            ,AnnotationNode(name = Option("RestResource"),
                body = Option(AnnotationParameterList(("urlMapping", "/yourUrl"))), Range.INVALID_LOCATION)
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
    def getParameters: Seq[String]
}
case class AnnotationValue(value: String, range: Range) extends AnnotationBody {
    override def nodeType: AstNodeType = AnnotationParameterNodeType

    override def getParameters: Seq[String] = Seq(value)
}
case class AnnotationParameter(name: String, value: String, range: Range, valueType: AnnotationParameterValueType = AnnotationParameterValueString) extends AstNode {
    override def nodeType: AstNodeType = AnnotationParameterNodeType
    def printBody: String = {
        valueType match {
            case AnnotationParameterValueString => name + "='" + value + "'"
            case _ => name + "=" + value
        }
    }
}
sealed trait AnnotationParameterValueType
case object AnnotationParameterValueString extends AnnotationParameterValueType
case object AnnotationParameterValueBoolean extends AnnotationParameterValueType

object AnnotationParameter {
    def apply(name: String, value: String): AnnotationParameter = AnnotationParameter(name, value, Range.INVALID_LOCATION)
    def apply(name: String, value: String, valueType: AnnotationParameterValueType): AnnotationParameter = AnnotationParameter(name, value, Range.INVALID_LOCATION, valueType)
}
case class AnnotationParameterList(params: List[AnnotationParameter], range: Range) extends AnnotationBody {
    override def getParameters: Seq[String] = params.map(p => p.printBody)
}

object AnnotationParameterList {
    def apply(param: (String, String)): AnnotationParameterList = AnnotationParameterList(List(AnnotationParameter(param._1, param._2, Range.INVALID_LOCATION)), Range.INVALID_LOCATION)
    def apply(param: (String, String, AnnotationParameterValueType)): AnnotationParameterList = AnnotationParameterList(List(AnnotationParameter(param._1, param._2, Range.INVALID_LOCATION, param._3)), Range.INVALID_LOCATION)
    def apply(params: List[AnnotationParameter]): AnnotationParameterList = AnnotationParameterList(params, Range.INVALID_LOCATION)
}

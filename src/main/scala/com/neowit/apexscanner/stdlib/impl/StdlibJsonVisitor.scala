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

import com.neowit.apexscanner.antlr.{ApexParserUtils, ApexcodeParser}
import com.neowit.apexscanner.ast.ASTBuilderVisitor
import com.neowit.apexscanner.Project
import com.neowit.apexscanner.nodes.{AstNode, ClassNode, DataTypeNode, DocNode, IdentifierNode, MethodNodeBase, MethodParameterNode, NamespaceNode, Range, ValueType}
import com.typesafe.scalalogging.LazyLogging

/**
  * Created by Andrey Gavrikov 
  */
class StdlibJsonVisitor(project: Project) extends StdlibJsonBaseVisitor[AstNode] with LazyLogging {
    val astBuilderVisitor = new ASTBuilderVisitor(project, documentOpt = None)

    /**
      * this is the main method which should be called after Apex API JSON file is parsed
      * @param apexApiJson root node of Apex API definition
      * @return
      */
    def visit(apexApiJson: ApexApiJson): Project = {
        apexApiJson.publicDeclarations.foreach{
            case (name, jsonNamespace) =>
                val namespace = visitApexApiJsonNamespace(name, jsonNamespace)
                project.addByQualifiedName(namespace.asInstanceOf[NamespaceNode])
        }
        project
    }

    override def visitApexApiJsonNamespace(name: String, context: ApexApiJsonNamespace): AstNode = {
        val namespace = NamespaceNode(Option(name))
        context.classMap.foreach{
            case (clsName, cls) =>
                val clsNode = visitApexApiJsonClass(clsName, cls)
                namespace.addChildToAst(clsNode)
                project.addByQualifiedName(clsNode.asInstanceOf[ClassNode])
        }
        namespace
    }
    override def visitApexApiJsonClass(name: String, context: ApexApiJsonClass): AstNode = {
        val cls = ClassNode(Range.INVALID_LOCATION)
        cls.addChildToAst(IdentifierNode(name, Range.INVALID_LOCATION))
        context.constructors.foreach{n =>
            val constructor = visitApexApiJsonMethod(n)
            cls.addChildToAst(constructor)
        }
        context.methods.foreach{n =>
            val m = visitApexApiJsonMethod(n)
            cls.addChildToAst(m)
        }
        /*
        context.properties.foreach{n =>
            val m = visitApexApiJsonProperty(n)
            cls.addChildToAst(m)
        }
        */
        cls
    }

    override def visitApexApiJsonMethod(context: ApexApiJsonMethod): AstNode = {
        val m = new MethodNodeBase { self =>
            private val returnType: Option[DataTypeNode] = context.returnType.map(visitDataType)

            override def nameOpt: Option[String] = Option(context.name)

            override def isAbstract: Boolean = false

            override def isStatic: Boolean = context.isStatic.contains(true)

            override def getValueType: Option[ValueType] = {
                returnType.map(_.getDataType)
            }

            override def range: Range = Range.INVALID_LOCATION

            override protected def resolveDefinitionImpl(): Option[AstNode] = Option(this)

            override def getApexDoc: Option[DocNode] = {
                context.methodDoc match {
                    case Some(text) =>
                        Option(DocNode(text, Range.INVALID_LOCATION))
                    case None => None
                }
            }

            override protected def getSelf: AstNode = self
        }

        m
    }

    //TODO figure out the purpose of property in APEX API
    //  "properties" : [ {
    override def visitApexApiJsonProperty(context: ApexApiJsonProperty): AstNode = {
        ???
    }

    override def visitApexApiJsonMethodParameter(context: ApexApiJsonMethodParameter): AstNode = {
        val p = MethodParameterNode(context.name, Range.INVALID_LOCATION)

        val parameterTypeNode = visitDataType(context.`type`)
        p.addChildToAst(parameterTypeNode)

        p
    }

    private def visitDataType(text: String): DataTypeNode = {
        // visit expression to try and build valid DataTypeNode
        val parser = createParser(text)
        val tree = parser.dataType()
        astBuilderVisitor.visitDataType(tree).asInstanceOf[DataTypeNode]
    }

    private def createParser(text: String): ApexcodeParser = {
        val _tokens = ApexParserUtils.getTokensFromText(text)
        val parser = new ApexcodeParser(_tokens)
        ApexParserUtils.removeConsoleErrorListener(parser)
        parser
    }


}

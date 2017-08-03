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

import com.neowit.apexscanner.antlr.{ApexParserUtils, ApexcodeParser}
import com.neowit.apexscanner.ast.ASTBuilderVisitor
import com.neowit.apexscanner.extlib.CodeLibrary
import com.neowit.apexscanner.nodes.{AstNode, ClassNode, DataTypeNode, DocNode, EnumConstantNode, EnumNode, IdentifierNode, MethodNodeBase, MethodParameterNode, NamespaceNode, Range, ValueType}
import com.typesafe.scalalogging.LazyLogging

/**
  * Created by Andrey Gavrikov 
  */
class StdlibJsonVisitor(lib: CodeLibrary) extends StdlibJsonBaseVisitor[AstNode] with LazyLogging {
    val astBuilderVisitor = new ASTBuilderVisitor(projectOpt = None, documentOpt = None)

    /**
      * this is the main method which should be called after Apex API JSON file is parsed
      * @param apexApiJson root node of Apex API definition
      * @return
      */
    def visit(apexApiJson: ApexApiJson): CodeLibrary = {
        apexApiJson.publicDeclarations.foreach{
            case (name, jsonNamespace) =>
                val namespace = visitApexApiJsonNamespace(name, jsonNamespace)
                lib.addByQualifiedName(namespace.asInstanceOf[NamespaceNode])
        }
        lib
    }

    override def visitApexApiJsonNamespace(name: String, context: ApexApiJsonNamespace): AstNode = {
        val namespace = NamespaceNode(Option(name))
        // tooling API does not return ENUMS correctly
        // it returns them as normal classes , e.g. ApexPages.Severity
        // so having to detect Enums by name
        context.classMap.foreach{
            case (clsName, cls) if isEnum(clsName)=>
                val enumNode = visitApexApiJsonEnum(clsName, cls)
                namespace.addChildToAst(enumNode)

            case (clsName, cls) =>
                val clsNode = visitApexApiJsonClass(clsName, cls)
                //logger.debug(clsNode.toString)
                namespace.addChildToAst(clsNode)
                lib.addByQualifiedName(clsNode.asInstanceOf[ClassNode])
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

    def visitApexApiJsonEnum(name: String, context: ApexApiJsonClass): AstNode = {
        val enum = EnumNode(Range.INVALID_LOCATION)
        enum.addChildToAst(IdentifierNode(name, Range.INVALID_LOCATION))
        context.methods.foreach{n =>
            val m = visitApexApiJsonMethod(n)
            enum.addChildToAst(m)
        }

        // add standard ENUM method
        EnumNode.addStandardMethods(enum)

        context.properties.foreach{n =>
            val m = visitApexApiJsonEnumConstant(n)
            enum.addChildToAst(m)
        }
        enum
    }

    private val ENUM_CLASS_NAMES = Set("Severity")
    private def isEnum(className: String): Boolean = {
        className.endsWith("Enum") || ENUM_CLASS_NAMES.contains(className)
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


            override def isMethodLike: Boolean = true

            override def methodParameters: Seq[String] = context.parameters.map(_.`type`)

            override def symbolValueType: Option[String] = context.returnType

            override def symbolIsStatic: Boolean = context.isStatic.getOrElse(false)

            override protected def getSelf: AstNode = self
        }

        m
    }

    //TODO figure out the purpose of property in APEX API
    // Tooling API is not consistent in what it returns as "properties"
    // it often returns ENUM values as properties
    //  e.g. ApexPages.Severity
    // but also returns actual object properties
    //  e.g. Component.childComponents
    //  "properties" : [ {
    override def visitApexApiJsonProperty(context: ApexApiJsonProperty): AstNode = {
        ???
    }
    def visitApexApiJsonEnumConstant(context: ApexApiJsonProperty): AstNode = {
        val node = EnumConstantNode(context.name, Range.INVALID_LOCATION)
        EnumConstantNode.addStandardMethods(node)
        node
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

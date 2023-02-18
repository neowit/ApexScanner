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
import com.neowit.apexscanner.ast.ApexAstBuilderVisitor
import com.neowit.apexscanner.extlib.CodeLibrary
import com.neowit.apexscanner.nodes.{AstNode, ClassNode, DataTypeNode, DocNode, EnumConstantNode, EnumNode, IdentifierNode, MethodNodeBase, MethodParameterNode, NamespaceNode, Range, ValueType}
import com.neowit.apexscanner.symbols
import com.typesafe.scalalogging.LazyLogging

/**
  * Created by Andrey Gavrikov 
  */
class StdlibJsonVisitor(lib: CodeLibrary) extends StdlibJsonBaseVisitor[AstNode] with LazyLogging {
    private val astBuilderVisitor = new ApexAstBuilderVisitor(projectOpt = None, documentOpt = None, tokenStreamOpt = None)
    private var _systemNamespaceNode: Option[NamespaceNode] = None
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
        val namespace = new NamespaceNode(Option(name)) {
            override def getSymbolsForCompletion: Seq[symbols.Symbol] = {
                name match {
                    case Some(namespaceName)  =>
                        getSystemSymbols(lib, namespaceName) ++ super.getSymbolsForCompletion
                    case _ =>
                        super.getSymbolsForCompletion
                }
            }

            override def findChildInAst(filter: (AstNode) => Boolean): Option[AstNode] = {
                super.findChildInAst(filter).orElse{
                    name match {
                        case Some(namespaceName) if "System" != namespaceName =>
                            findChildInSystem(lib, namespaceName, filter)
                        case _ =>
                            None
                    }
                }

            }
        }
        if ("system" == name.toLowerCase) {
            _systemNamespaceNode = Option(namespace)
        }
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

    /**
      * special handling for classes under namespace "System"
      * classes under namespace System are allowed to be referenced without namespace name
      * e.g. System.Database.insert() is the same as Database.insert()
      * in order to resolve such cases we have to do additional query inside "System" namespace
      * @param name potential class name under namespace "System"
      * @return
      */
    private def getSystemSymbols(lib: CodeLibrary, name: String): Seq[com.neowit.apexscanner.symbols.Symbol] = {
        if (null == name || name.isEmpty) {
            Seq.empty
        } else {
            val nameLower = name.toLowerCase
            _systemNamespaceNode match {
                case Some(systemNamespaceNode) =>
                    systemNamespaceNode.findChildInAst{
                        case ClassNode(Some(className), _) => className.toLowerCase == nameLower
                        case _ => false
                    }.map(_.getSymbolsForCompletion).getOrElse(Seq.empty)
                case None => Seq.empty
            }
        }
    }

    /**
      * special handling for classes under namespace "System"
      * classes under namespace System are allowed to be referenced without namespace name
      * e.g. System.Database.insert() is the same as Database.insert()
      * in order to resolve such cases we have to do additional query inside "System" namespace
      * @param filter filter to apply to find a child under namespace "System"
      * @return
      */
    private def findChildInSystem(lib: CodeLibrary, namespaceName: String, filter: (AstNode) => Boolean): Option[AstNode] = {
        if (null == namespaceName || namespaceName.isEmpty) {
            None
        } else {
            val nameLower = namespaceName.toLowerCase
            _systemNamespaceNode match {
                case Some(systemNamespaceNode) =>
                    systemNamespaceNode.findChildInAst{
                        case ClassNode(Some(className), _) => className.toLowerCase == nameLower
                        case _ => false
                    }.flatMap(_.findChildInAst(filter))
                case None => None
            }

        }
    }

    override def visitApexApiJsonClass(name: String, context: ApexApiJsonClass): AstNode = {
        val cls = ClassNode(Option(name), Range.INVALID_LOCATION)
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
        val enumNode = EnumNode(Option(name), Range.INVALID_LOCATION)
        enumNode.addChildToAst(IdentifierNode(name, Range.INVALID_LOCATION))
        context.methods.foreach{n =>
            val m = visitApexApiJsonMethod(n)
            enumNode.addChildToAst(m)
        }

        // add standard ENUM method
        EnumNode.addStandardMethods(enumNode)

        context.properties.foreach{n =>
            val m = visitApexApiJsonEnumConstant(n)
            enumNode.addChildToAst(m)
        }
        enumNode
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
                getProvidedValueType match {
                    case _providedValueTypeOpt @ Some(_) => _providedValueTypeOpt
                    case None =>
                        returnType.map(_.getDataType)
                }
            }

            override def range: Range = Range.INVALID_LOCATION

            override protected def resolveDefinitionImpl(actionContext: com.neowit.apexscanner.scanner.actions.ActionContext): Option[AstNode] = Option(this)

            override def getApexDoc: Option[DocNode] = {
                context.methodDoc match {
                    case Some(text) =>
                        Option(DocNode(text, Range.INVALID_LOCATION))
                    case None => None
                }
            }


            override def isMethodLike: Boolean = true

            override def methodParameters: Seq[String] = context.parameters.map(_.`type`).toIndexedSeq

            override def symbolValueType: Option[String] = getValueType.map(_.toString)//context.returnType

            override def symbolIsStatic: Boolean = context.isStatic.getOrElse(false)

            override def visibility: Option[String] = Option("public")

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

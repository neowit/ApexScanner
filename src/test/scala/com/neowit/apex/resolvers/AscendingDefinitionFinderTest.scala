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

package com.neowit.apex.resolvers

import java.nio.file.{FileSystems, Path}

import com.neowit.apex.{Project, TestConfigProvider}
import com.neowit.apex.ast.AstBuilder
import com.neowit.apex.nodes._
import org.scalatest.FunSuite

/**
  * Created by Andrey Gavrikov 
  */
class AscendingDefinitionFinderTest extends FunSuite with TestConfigProvider {

    def withPathProperty(pathKey: String)(codeBlock: (String, Path, AstNode) => Any): Unit = {
        val filePath = getProperty("AscendingDefinitionFinderTest.testFindDefinition.path")
        val path = FileSystems.getDefault.getPath(filePath)

        val astBuilder = new AstBuilder(Project(path))
        astBuilder.build(path)

        astBuilder.getAst(path) match {
            case None =>
            // do nothing
            case Some(result) if result.fileScanResult.errors.nonEmpty =>
                println("ERRORS ENCOUNTERED")
                result.fileScanResult.errors.foreach(println(_))

            case Some(result) =>
                val rootNode = result.rootNode
                codeBlock(filePath, path, rootNode)
        }
    }

    test("testFindDefinition - inner class Variables") {
        withPathProperty("AscendingDefinitionFinderTest.testFindDefinition.path"){ (filePath, path, rootNode) =>
            // inner class variable
            val testTag = "#findVarFromInnerClass#"
            val lineNos = getLineNoByTag(path, testTag)
            assertResult(1, s"Invalid test data, expected to find line with tag: $testTag in file: " + filePath)(lineNos.length)
            val lineNo = lineNos.head

            val typeNameStr =
                getNodeDefinition(testTag, rootNode, Position(lineNo, 32)) match {
                    case nodes if nodes.nonEmpty =>
                        assertResult(1, "Expected exactly 1 local variable definition")(nodes.length)
                        val node = nodes.head
                        assertResult(LocalVariableNodeType)(node.asInstanceOf[AstNode].nodeType)

                        node.asInstanceOf[IsTypeDefinition].getValueType.map(_.toString).getOrElse("NOT FOUND")
                    case _ => "NOT FOUND"
                }
            assert(typeNameStr.nonEmpty, testTag + ": NOT FOUND")
            assertResult("String")(typeNameStr)
        }
    }

    ignore("testFindDefinition - for Variables") {
        val filePath = getProperty("AscendingDefinitionFinderTest.testFindDefinition.path")
        val path = FileSystems.getDefault.getPath(filePath)

        val astBuilder = new AstBuilder(Project(path))
        astBuilder.build(path)

        astBuilder.getAst(path) match {
            case None =>
            // do nothing
            case Some(result) if result.fileScanResult.errors.nonEmpty =>
                println("ERRORS ENCOUNTERED")
                result.fileScanResult.errors.foreach(println(_))

            case Some(result) =>
                val rootNode = result.rootNode
                // local variable
                var testTag = "#findLocalVariableType"
                var lineNos = getLineNoByTag(path, testTag)
                assertResult(1, s"Invalid test data, expected to find line with tag: $testTag in file: " + filePath)(lineNos.length)
                var lineNo = lineNos.head

                val typeNameInt =
                    getNodeDefinition(testTag, rootNode, Position(lineNo, 28)) match {
                        case nodes if nodes.nonEmpty =>
                            assertResult(1, "Expected exactly 1 local variable definition")(nodes.length)
                            val node = nodes.head
                            assertResult(LocalVariableNodeType)(node.asInstanceOf[AstNode].nodeType)

                            node.asInstanceOf[IsTypeDefinition].getValueType.map(_.toString).getOrElse("NOT FOUND")
                        case _ => "NOT FOUND"
                    }
                assert(typeNameInt.nonEmpty, testTag + ": NOT FOUND")
                assertResult("Integer")(typeNameInt)

                // class variable
                testTag = "#findClassVariableType"
                lineNos = getLineNoByTag(path, testTag)
                assertResult(1, s"Invalid test data, expected to find line with tag: $testTag in file: " + filePath)(lineNos.length)
                lineNo = lineNos.head
                val typeNameStr =
                    getNodeDefinition(testTag, rootNode, Position(lineNo, 33)) match {
                        case nodes if nodes.nonEmpty =>
                            assertResult(1, "Expected exactly 1 class variable definition")(nodes.length)
                            val node = nodes.head
                            assertResult(ClassVariableNodeType)(node.asInstanceOf[AstNode].nodeType)

                            node.asInstanceOf[IsTypeDefinition].getValueType.map(_.toString).getOrElse("NOT FOUND")
                        case _ => "NOT FOUND"
                    }
                assert(typeNameStr.nonEmpty, testTag + ": NOT FOUND")
                assertResult("String")(typeNameStr)

                // method by name & No of Parameters without specific type
                /*
                lineNos = getLineNoByTag(path, "#findMethodType")
                val resLineNos = getLineNoByTag(path, "#result of findMethodType")
                assertResult(1, "Invalid test data, expected to find line with tag: #findMethodType in file: " + filePath)(lineNos.length)
                lineNo = lineNos.head
                val typeNameMethodFuzzy =
                    getNodeDefinition("#findMethodType", rootNode, Position(lineNo, 20)) match {
                        case nodes if nodes.nonEmpty =>
                            assertResult(resLineNos.length, s". Expected exactly ${resLineNos.length} methods (as we do not yet resolve parameter types)")(nodes.length)
                            val node = nodes.head
                            assertResult(MethodNodeType)(node.asInstanceOf[AstNode].nodeType)

                            node.asInstanceOf[IsTypeDefinition].getValueType.map(_.toString).getOrElse("NOT FOUND")
                        case _ => "NOT FOUND"
                    }
                assertResult("M2Type")(typeNameMethodFuzzy)
                */

                // method by name & parameter types
                testTag = "#findMethodType_int_str#"
                lineNos = getLineNoByTag(path, testTag)
                assertResult(1, s"Invalid test data, expected to find line with tag: $testTag in file: " + filePath)(lineNos.length)
                lineNo = lineNos.head
                var typeNameMethod =
                    getNodeDefinition(testTag, rootNode, Position(lineNo, 20)) match {
                        case nodes if nodes.nonEmpty =>
                            assertResult(1, s". Expected exactly 1 method")(nodes.length)
                            val node = nodes.head
                            assertResult(MethodNodeType)(node.asInstanceOf[AstNode].nodeType)

                            node.asInstanceOf[IsTypeDefinition].getValueType.map(_.toString).getOrElse("NOT FOUND")
                        case _ => "NOT FOUND"
                    }
                assert(typeNameMethod.nonEmpty, testTag + ": NOT FOUND")
                assertResult("M2Type")(typeNameMethod)

                // method by name & parameter types & this
                testTag = "#findMethodType_int_str_bool#"
                lineNos = getLineNoByTag(path, testTag)
                assertResult(1, s"Invalid test data, expected to find line with tag: $testTag in file: " + filePath)(lineNos.length)
                lineNo = lineNos.head
                typeNameMethod =
                    getNodeDefinition(testTag, rootNode, Position(lineNo, 20)) match {
                        case nodes if nodes.nonEmpty =>
                            assertResult(1, s". Expected exactly 1 method")(nodes.length)
                            val node = nodes.head
                            assertResult(MethodNodeType)(node.asInstanceOf[AstNode].nodeType)

                            node.asInstanceOf[IsTypeDefinition].getValueType.map(_.toString).getOrElse("NOT FOUND")
                        case _ => "NOT FOUND"
                    }
                assert("NOT FOUND" != typeNameMethod, testTag + ": NOT FOUND")
                assertResult("M3Type")(typeNameMethod)
        }
    }

    private def getNodeDefinition(hint: String, rootNode: AstNode, location: Position): Seq[AstNode] = {
        val finder = new AscendingDefinitionFinder()
        finder.findDefinition(rootNode, location)  match {
            case nodes if nodes.nonEmpty =>
                println("FOUND NODES")
                nodes.foreach{node =>
                    println(" "  + node)
                    node match {
                        case n:IsTypeDefinition =>
                            println("   type: " + n.getValueType.map(_.toString).getOrElse("NOT FOUND"))
                        case n =>
                            assert(false, hint + ": WARNING - NOT IMPLEMENTED - this is not HasTypeDefinition node ")
                    }
                }
                nodes
            case _ =>
                Seq.empty
        }
    }


}

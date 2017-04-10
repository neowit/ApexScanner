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

import java.nio.file.FileSystems

import com.neowit.apex.{Project, TestConfigProvider}
import com.neowit.apex.ast.AstBuilder
import com.neowit.apex.nodes._
import org.scalatest.FunSuite

/**
  * Created by Andrey Gavrikov 
  */
class AscendingDefinitionFinderTest extends FunSuite with TestConfigProvider {

    test("testFindDefinition - for Variables") {
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
                var lineNos = getLineNoByTag(path, "#findLocalVariableType")
                assertResult(1, "Invalid test data, expected to find line with tag: #findLocalVariableType in file: " + filePath)(lineNos.length)
                var lineNo = lineNos.head

                val typeNameInt =
                    getNodeDefinition("#findLocalVariableType", rootNode, Position(lineNo, 28)) match {
                        case nodes if nodes.nonEmpty =>
                            assertResult(1, "Expected exactly 1 local variable definition")(nodes.length)
                            val node = nodes.head
                            assertResult(LocalVariableNodeType)(node.asInstanceOf[AstNode].nodeType)

                            node.asInstanceOf[IsTypeDefinition].getValueType.map(_.toString).getOrElse("NOT FOUND")
                        case _ => "NOT FOUND"
                    }
                assertResult("Integer")(typeNameInt)

                // class variable
                lineNos = getLineNoByTag(path, "#findClassVariableType")
                assertResult(1, "Invalid test data, expected to find line with tag: #findClassVariableType in file: " + filePath)(lineNos.length)
                lineNo = lineNos.head
                val typeNameStr =
                    getNodeDefinition("#findClassVariableType", rootNode, Position(lineNo, 33)) match {
                        case nodes if nodes.nonEmpty =>
                            assertResult(1, "Expected exactly 1 class variable definition")(nodes.length)
                            val node = nodes.head
                            assertResult(ClassVariableNodeType)(node.asInstanceOf[AstNode].nodeType)

                            node.asInstanceOf[IsTypeDefinition].getValueType.map(_.toString).getOrElse("NOT FOUND")
                        case _ => "NOT FOUND"
                    }
                assertResult("String")(typeNameStr)

                // method by name & No of Parameters without specific type
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

                /*
                // method by name & parameter types
                lineNos = getLineNoByTag(path, "#findMethodType_int_str")
                assertResult(1, "Invalid test data, expected to find line with tag: #findMethodType in file: " + filePath)(lineNos.length)
                lineNo = lineNos.head
                val typeNameMethod =
                    getNodeDefinition("#findMethodType", rootNode, Position(lineNo, 20)) match {
                        case nodes if nodes.nonEmpty =>
                            assertResult(1, s". Expected exactly 1 method")(nodes.length)
                            val node = nodes.head
                            assertResult(MethodNodeType)(node.asInstanceOf[AstNode].nodeType)

                            node.asInstanceOf[HasTypeDefinition].getType.map(_.toString).getOrElse("NOT FOUND")
                        case _ => "NOT FOUND"
                    }
                assertResult("M2Type")(typeNameMethod)
                */
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
                assert(false, hint + ": NOT FOUND")
                Seq.empty
        }
    }


}

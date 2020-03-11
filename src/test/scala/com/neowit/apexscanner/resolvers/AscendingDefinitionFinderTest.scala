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

package com.neowit.apexscanner.resolvers

import com.neowit.apexscanner.TestConfigProvider
import com.neowit.apexscanner.nodes._
import com.neowit.apexscanner.scanner.actions.{ActionContext, FindSymbolActionType}
import org.scalatest.funsuite.AnyFunSuite

import scala.util.Random

/**
  * Created by Andrey Gavrikov 
  */
// do not add any more tests to this class, these are difficult to maintain
class AscendingDefinitionFinderTest extends AnyFunSuite with TestConfigProvider {

    test("Multi-File variables - other class") {
        val projectData = ResolverTestUtils.getResolverTestData("AscendingDefinitionFinderTest.testFindMultiFileDefinition.Project.path")
        val testFileData = ResolverTestUtils.getResolverTestData("AscendingDefinitionFinderTest.testFindMultiFileDefinition.CurrentFile.path", Option(projectData.project))
        // other class variable
        val testTag = "#findVarFromAnotherClass#"
        val lineNos = getLineNoByTag(testFileData.path, testTag)
        assertResult(1, s"Invalid test data, expected to find line with tag: $testTag in file: " + testFileData.filePath)(lineNos.length)
        val lineNo = lineNos.head

        val typeNameStr =
            getNodeDefinition(testTag, testFileData.rootNode.get, Position(lineNo, 28)) match {
                case nodes if nodes.nonEmpty =>
                    assertResult(1, "Expected exactly 1 local variable definition")(nodes.length)
                    val node = nodes.head
                    assertResult(ClassVariableNodeType)(node.asInstanceOf[AstNode].nodeType)

                    node.asInstanceOf[IsTypeDefinition].getValueType.map(_.toString).getOrElse("NOT FOUND")
                case _ => "NOT FOUND"
            }
        assert(typeNameStr.nonEmpty, testTag + ": NOT FOUND")
        assertResult("Boolean")(typeNameStr)
    }

    test("Multi-File variables - other class -> inner class") {
        val projectData = ResolverTestUtils.getResolverTestData("AscendingDefinitionFinderTest.testFindMultiFileDefinition.Project.path")
        val testFileData = ResolverTestUtils.getResolverTestData("AscendingDefinitionFinderTest.testFindMultiFileDefinition.CurrentFile.path", Option(projectData.project))
        // inner class variable
        val testTag = "#findVarFromInnerClassOfAnotherClass#"
        val lineNos = getLineNoByTag(testFileData.path, testTag)
        assertResult(1, s"Invalid test data, expected to find line with tag: $testTag in file: " + testFileData.filePath)(lineNos.length)
        val lineNo = lineNos.head

        val typeNameStr =
            getNodeDefinition(testTag, testFileData.rootNode.get, Position(lineNo, 30)) match {
                case nodes if nodes.nonEmpty =>
                    assertResult(1, "Expected exactly 1 local variable definition")(nodes.length)
                    val node = nodes.head
                    assertResult(ClassVariableNodeType)(node.asInstanceOf[AstNode].nodeType)

                    node.asInstanceOf[IsTypeDefinition].getValueType.map(_.toString).getOrElse("NOT FOUND")
                case _ => "NOT FOUND"
            }
        assert(typeNameStr.nonEmpty, testTag + ": NOT FOUND")
        assertResult("String")(typeNameStr)
    }

    test("FindDefinition - inner class Variables") {
        val testFileData = ResolverTestUtils.getResolverTestData("AscendingDefinitionFinderTest.testFindDefinition.path")
        // inner class variable
        val testTag = "#findVarFromInnerClass#"
        val lineNos = getLineNoByTag(testFileData.path, testTag)
        assertResult(1, s"Invalid test data, expected to find line with tag: $testTag in file: " + testFileData.filePath)(lineNos.length)
        val lineNo = lineNos.head

        val typeNameStr =
            getNodeDefinition(testTag, testFileData.rootNode.get, Position(lineNo, 32)) match {
                case nodes if nodes.nonEmpty =>
                    assertResult(1, "Expected exactly 1 local variable definition")(nodes.length)
                    val node = nodes.head
                    assertResult(ClassVariableNodeType)(node.asInstanceOf[AstNode].nodeType)

                    node.asInstanceOf[IsTypeDefinition].getValueType.map(_.toString).getOrElse("NOT FOUND")
                case _ => "NOT FOUND"
            }
        assert(typeNameStr.nonEmpty, testTag + ": NOT FOUND")
        assertResult("String")(typeNameStr)
    }


    test("FindDefinition - local method with params") {
        val testFileData = ResolverTestUtils.getResolverTestData("AscendingDefinitionFinderTest.testFindDefinition.path")
        // method by name & parameter types
        val testTag = "#findMethodType_int_str#"
        val lineNos = getLineNoByTag(testFileData.path, testTag)
        assertResult(1, s"Invalid test data, expected to find line with tag: $testTag in file: " + testFileData.filePath)(lineNos.length)
        val lineNo = lineNos.head
        val typeNameMethod =
            getNodeDefinition(testTag, testFileData.rootNode.get, Position(lineNo, 20)) match {
                case nodes if nodes.nonEmpty =>
                    assertResult(1, s". Expected exactly 1 method")(nodes.length)
                    val node = nodes.head
                    assertResult(MethodNodeType)(node.asInstanceOf[AstNode].nodeType)

                    node.asInstanceOf[IsTypeDefinition].getValueType.map(_.toString).getOrElse("NOT FOUND")
                case _ => "NOT FOUND"
            }
        assert(typeNameMethod.nonEmpty, testTag + ": NOT FOUND")
        assertResult("M2Type")(typeNameMethod)
    }

    test("FindDefinition - local method with params & this") {
        val testFileData = ResolverTestUtils.getResolverTestData("AscendingDefinitionFinderTest.testFindDefinition.path")
        // method by name & parameter types & this
        val testTag = "#findMethodType_int_str_bool#"
        val lineNos = getLineNoByTag(testFileData.path, testTag)
        assertResult(1, s"Invalid test data, expected to find line with tag: $testTag in file: " + testFileData.filePath)(lineNos.length)
        val lineNo = lineNos.head
        val typeNameMethod =
            getNodeDefinition(testTag, testFileData.rootNode.get, Position(lineNo, 20)) match {
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

    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    private def getNodeDefinition(hint: String, rootNode: AstNode, location: Position): Seq[AstNode] = {
        val actionContext = ActionContext("AscendingDefinitionFinderTest-" + Random.nextString(5), FindSymbolActionType)
        val finder = new AscendingDefinitionFinder(actionContext)
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

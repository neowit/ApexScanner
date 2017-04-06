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
                    getVariableDefinition("#findLocalVariableType", rootNode, Position(lineNo, 28)) match {
                        case Some(node) =>
                            assertResult(LocalVariableNodeType)(node.asInstanceOf[AstNode].nodeType)
                            node.getType.map(_.toString).getOrElse("NOT FOUND")
                        case None => "NOT FOUND"
                    }
                assertResult("Integer")(typeNameInt)

                // class variable
                lineNos = getLineNoByTag(path, "#findClassVariableType")
                assertResult(1, "Invalid test data, expected to find line with tag: #findClassVariableType in file: " + filePath)(lineNos.length)
                lineNo = lineNos.head
                val typeNameStr =
                    getVariableDefinition("#findClassVariableType", rootNode, Position(lineNo, 33)) match {
                        case Some(node) =>
                            assertResult(ClassVariableNodeType)(node.asInstanceOf[AstNode].nodeType)
                            node.getType.map(_.toString).getOrElse("NOT FOUND")
                        case None => "NOT FOUND"
                    }
                assertResult("String")(typeNameStr)

                // class variable
                lineNos = getLineNoByTag(path, "#findMethodType")
                assertResult(1, "Invalid test data, expected to find line with tag: #findMethodType in file: " + filePath)(lineNos.length)
                lineNo = lineNos.head
                val typeNameMethod =
                    getVariableDefinition("#findMethodType", rootNode, Position(lineNo, 20)) match {
                        case Some(node) =>
                            assertResult(MethodNodeType)(node.asInstanceOf[AstNode].nodeType)
                            node.getType.map(_.toString).getOrElse("NOT FOUND")
                        case None => "NOT FOUND"
                    }
                assertResult("M2Type")(typeNameMethod)
        }
    }

    private def getVariableDefinition(hint: String, rootNode: AstNode, location: Position): Option[HasTypeDefinition] = {
        val finder = new AscendingDefinitionFinder()
        finder.findDefinition(rootNode, location)  match {
            case Some(node: HasTypeDefinition) =>
                println(hint + ": FOUND: " + node)
                println("   type: " + node.getType.map(_.toString).getOrElse("NOT FOUND"))
                return Option(node)
            case Some(node) =>
                println(hint + ": FOUND: " + node)
                assert(false, hint + ": WARNING - NOT IMPLEMENTED - this is not HasTypeDefinition node ")
            case _ =>
                assert(false, hint + ": NOT FOUND")
        }
        None
    }


}

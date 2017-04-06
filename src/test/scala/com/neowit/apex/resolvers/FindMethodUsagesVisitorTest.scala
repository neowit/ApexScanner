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
import com.neowit.apex.ast.{AstBuilder, AstWalker}
import com.neowit.apex.nodes.{DataType, QualifiedName}
import org.scalatest.FunSuite

/**
  * Created by Andrey Gavrikov 
  */
class FindMethodUsagesVisitorTest extends FunSuite with TestConfigProvider {
    case class DataTypeForTest(typeName: String, typeArguments: Seq[DataType] = Seq.empty) extends DataType {
        override def qualifiedName: QualifiedName = QualifiedName(Array(typeName))
    }

    test("testMethodUsages") {
        val filePath = getProperty("FindMethodUsagesVisitorTest.testFindUsages.path")
        val path = FileSystems.getDefault.getPath(filePath)

        val lineNos = getLineNoByTag(path, "#result")
        assert(lineNos.nonEmpty, "Invalid test data, expected to find line(s) with tag: #result in file: " + filePath)

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
                val findMethodVisitor = new FindMethodVisitor(QualifiedName(Array("method2")), List(DataTypeForTest("integer"), DataTypeForTest("list", Seq(DataTypeForTest("String")))))
                new AstWalker().walk(rootNode, findMethodVisitor)
                findMethodVisitor.getFoundMethod match {
                    case Some(methodNode) =>
                        val findUsagesVisitor = new FindMethodUsagesVisitor(methodNode)
                        new AstWalker().walk(rootNode, findUsagesVisitor)
                        val results = findUsagesVisitor.getResult
                        results.foreach(methodCallNode => println("FOUND: " + methodCallNode))
                        val foundLines = results.map(_.range.start.line)
                        // check if there is exist a result line which does not match expected line
                        assertResult(lineNos.length, "Number of expected and found results does not match")(foundLines.intersect(lineNos).length)

                    case None =>
                        assert(false, "NO USAGES FOUND")
                }
        }
    }

}

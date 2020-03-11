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
import com.neowit.apexscanner.ast.{AstWalker, QualifiedName}
import com.neowit.apexscanner.nodes.ValueType
import com.neowit.apexscanner.scanner.actions.{ActionContext, FindUsagesActionType}
import org.scalatest.funsuite.AnyFunSuite

import scala.util.Random

/**
  * Created by Andrey Gavrikov 
  */
class FindMethodUsagesVisitorTest extends AnyFunSuite with TestConfigProvider {
    case class ValueTypeForTest(typeName: String, typeArguments: Seq[ValueType] = Seq.empty) extends ValueType {
        override def qualifiedName: QualifiedName = QualifiedName(Array(typeName))
    }

    test("Find MethodUsages") {
        val testFileData = ResolverTestUtils.getResolverTestData("FindMethodUsagesVisitorTest.testFindUsages.path")

        val testTag = "#findMethodUsages_result#"
        val lineNos = getLineNoByTag(testFileData.path, testTag)
        assert(lineNos.nonEmpty, s"Invalid test data, expected to find line(s) with tag: $testTag in file: " + testFileData.filePath)

        val actionContext = ActionContext("FindMethodUsagesVisitorTest-" + Random.nextString(5), FindUsagesActionType)
        val findMethodVisitor = new FindMethodVisitor(QualifiedName(Array("method2")), List(ValueTypeForTest("integer"), ValueTypeForTest("list", Seq(ValueTypeForTest("String")))), actionContext)
        new AstWalker().walk(testFileData.rootNode.get, findMethodVisitor)
        findMethodVisitor.getFoundMethod match {
            case Some(methodNode) =>
                val findUsagesVisitor = new FindMethodUsagesVisitor(methodNode, actionContext)
                new AstWalker().walk(testFileData.rootNode.get, findUsagesVisitor)
                val results = findUsagesVisitor.getResult
                results.foreach(methodCallNode => println("FOUND: " + methodCallNode))
                val foundLines = results.map(_.range.start.line)
                // check if there is exist a result line which does not match expected line
                assertResult(lineNos.length, "Number of expected and found results does not match")(foundLines.intersect(lineNos).length)

            case None =>
                assert(false, "Method not found ")
        }
    }

}

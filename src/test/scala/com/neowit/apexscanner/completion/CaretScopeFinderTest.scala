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

package com.neowit.apexscanner.completion

import java.nio.file.{FileSystems, Path, Paths}

import com.neowit.apexscanner.antlr.CaretUtils
import com.neowit.apexscanner.ast.QualifiedName
import com.neowit.apexscanner.nodes.MethodBodyNodeType
import com.neowit.apexscanner.{Project, TestConfigProvider}
import org.scalatest.FunSuite
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
/**
  * Created by Andrey Gavrikov
  *
  * unit tests for CompletionFinder.findCaretScope() logic
  */
class CaretScopeFinderTest extends FunSuite with TestConfigProvider with ScalaFutures with IntegrationPatience {
    private val filePath = getProperty("CompletionFinderTest.path")
    private val projectPath = FileSystems.getDefault.getPath(filePath)

    test("testFindCaretScope: `con.`") {
        val text =
            """
              |class CompletionTester {
              | public void testCompletion() {
              |		CompletionTester con = new CompletionTester();
              |     con.<CARET>
              | }
              |}
            """.stripMargin
        val result = findCaretScope(text, "testFindCaretScope.con").futureValue
        result match {
            case Some(FindCaretScopeResult(Some(CaretScope(_, Some(typeDefinition))), _)) =>
                assertResult(Some("CompletionTester"), "Wrong caret type detected. Expected 'CompletionTester'")(typeDefinition.getValueType.map(_.toString))
            case _ =>
                assert(false, "Failed to identify caret type. Expected 'CompletionTester'")
        }

    }

    test("testFindCaretScope: `con.acc() + `") {
        val text =
            """
              |class CompletionTester {
              | public void testCompletion() {
              |		SomeClass con = new SomeClass();
              |     con.acc() + <CARET>
              | }
              |}
            """.stripMargin
        val result = findCaretScope(text, "testFindCaretScope").futureValue
        result match {
            case Some(FindCaretScopeResult(Some(CaretScope(_, Some(typeDefinition))), _)) =>
                assertResult(None, "Wrong caret type detected.")(typeDefinition.getValueType.map(_.toString))
            case Some(FindCaretScopeResult(Some(CaretScope(scopeNode, None)), _)) =>
                assertResult(MethodBodyNodeType, "Wrong scope type")(scopeNode.nodeType)
                println(scopeNode)
            case x =>
                println(x)
                assert(false, "Failed to identify caret type")
        }
    }

    test("testFindCaretScope: `con.acc() + con.`") {
        val text =
            """
              |class CompletionTester {
              | public void testCompletion() {
              |		CompletionTester con = new CompletionTester();
              |     con.acc() + con.<CARET>
              | }
              |}
            """.stripMargin
        val result = findCaretScope(text, "testFindCaretScope.con").futureValue
        result match {
            case Some(FindCaretScopeResult(Some(CaretScope(_, Some(typeDefinition))), _)) =>
                assertResult(Some("CompletionTester"), "Wrong caret type detected. Expected 'CompletionTester'")(typeDefinition.getValueType.map(_.toString))
            case _ =>
                assert(false, "Failed to identify caret type. Expected 'CompletionTester'")
        }
    }

    test("testFindCaretScope: `con.opp.`") {
        val text =
            """
              |class CompletionTester {
              | Opportunity opp;
              | public void testCompletion() {
              |		CompletionTester con = new CompletionTester();
              |     con.opp.<CARET>
              | }
              |}
            """.stripMargin
        val result = findCaretScope(text, "testFindCaretScope").futureValue
        result match {
            case Some(FindCaretScopeResult(Some(CaretScope(_, Some(typeDefinition))), _)) =>
                assertResult(Some("Opportunity"), "Wrong caret type detected.")(typeDefinition.getValueType.map(_.toString))
            case _ =>
                assert(false, "Failed to identify caret type. Expected 'Opportunity'")
        }
    }

    test("testFindCaretScope: `con.inner.opp`") {
        val text =
            """
              |class CompletionTester {
              | class InnerClass {
              |     Opportunity opp;
              | }
              | InnerClass inner;
              | public void testCompletion() {
              |		CompletionTester con = new CompletionTester();
              |     con.inner.opp.<CARET>
              | }
              |}
            """.stripMargin
        val result = findCaretScope(text, "testFindCaretScope").futureValue
        result match {
            case Some(FindCaretScopeResult(Some(CaretScope(_, Some(typeDefinition))), _)) =>
                assertResult(Some("Opportunity"), "Wrong caret type detected.")(typeDefinition.getValueType.map(_.toString))
            case _ =>
                assert(false, "Failed to identify caret type. Expected 'Opportunity'")
        }
    }

    //TODO
    test("testFindCaretScope: `this.opp`") {
        val text =
            """
              |class CompletionTester {
              | Opportunity opp;
              |
              | this.opp.<CARET>
              |}
            """.stripMargin
        val result = findCaretScope(text, "testFindCaretScope").futureValue
        result match {
            case Some(FindCaretScopeResult(Some(CaretScope(_, Some(typeDefinition))), _)) =>
                assertResult(Some("Opportunity"), "Wrong caret type detected.")(typeDefinition.getValueType.map(_.toString))
            case _ =>
                assert(false, "Failed to identify caret type. Expected 'Opportunity'")
        }
    }


    //TODO
    test("testFindCaretScope: `this.inner.opp`") {
        val text =
            """
              |class CompletionTester {
              | class InnerClass {
              |     Opportunity opp;
              | }
              | InnerClass inner;
              | public void testCompletion() {
              |     this.inner.opp.<CARET>
              | }
              |}
            """.stripMargin
        val result = findCaretScope(text, "testFindCaretScope").futureValue
        result match {
            case Some(FindCaretScopeResult(Some(CaretScope(_, Some(typeDefinition))), _)) =>
                assertResult(Some("Opportunity"), "Wrong caret type detected.")(typeDefinition.getValueType.map(_.toString))
            case _ =>
                assert(false, "Failed to identify caret type. Expected 'Opportunity'")
        }
    }

    test("testFindCaretScope: `con.acc<CARET>`") {
        val text =
            """
              |class CompletionTester {
              | public void testCompletion() {
              |		CompletionTester con = new CompletionTester();
              |     con.acc<CARET>
              | }
              |}
            """.stripMargin
        val result = findCaretScope(text, "testFindCaretScope").futureValue
        result match {
            case Some(FindCaretScopeResult(Some(CaretScope(_, Some(typeDefinition))), _)) =>
                assertResult(Some("CompletionTester"), "Wrong caret type detected. Expected 'CompletionTester'")(typeDefinition.getValueType.map(_.toString))
            case _ =>
                assert(false, "Failed to identify caret type. Expected 'CompletionTester'")
        }
    }

    test("testFindCaretScope: `((Opportunity)obj).<CARET>`") {
        val text =
            """
              |class CompletionTester {
              | public void testCompletion() {
              |		CompletionTester con = new CompletionTester();
              |     ((Opportunity)obj).<CARET>
              | }
              |}
            """.stripMargin
        val result = findCaretScope(text, "testFindCaretScope").futureValue
        result match {
            case Some(FindCaretScopeResult(Some(CaretScope(_, Some(typeDefinition))), _)) =>
                assertResult(Some("Opportunity"), "Wrong caret type detected.")(typeDefinition.getValueType.map(_.toString))
            case _ =>
                assert(false, "Failed to identify caret type")
        }
    }

    test("testFindCaretScope: `new CompletionTester(<CARET>`") {
        val text =
            """
              |class CompletionTester {
              | public void testCompletion() {
              |     new CompletionTester(<CARET>
              | }
              |}
            """.stripMargin
        val result = findCaretScope(text, "testFindCaretScope").futureValue
        result match {
            case Some(FindCaretScopeResult(Some(CaretScope(_, Some(typeDefinition))), _)) =>
                assertResult(Some(QualifiedName(Array("CompletionTester"))), "Wrong caret type detected.")(typeDefinition.getValueType.map(_.qualifiedName))
            case _ =>
                assert(false, "Failed to identify caret type")
        }
    }
    test("testFindCaretScope: `List<Map<String, Set<Integer>>>`") {
        val text =
            """
              |class CompletionTester {
              | public void testCompletion() {
              |		CompletionTester con = new CompletionTester();
              |     new List<Map<String, Set<Integer>>>( <CARET> )
              | }
              |}
            """.stripMargin
        val result = findCaretScope(text, "testFindCaretScope3").futureValue
        result match {
            case Some(FindCaretScopeResult(Some(CaretScope(_, Some(typeDefinition))), _)) =>
                assertResult(Some("List<Map<String,Set<Integer>>>"), "Wrong caret type detected.")(typeDefinition.getValueType.map(_.toString))
            case _ =>
                assert(false, "Failed to identify caret type")
        }
    }

    test("testFindCaretScope: `String str = new List<Map<String, Set<Integer>>>(`") {
        val text =
            """
              |class CompletionTester {
              | public void testCompletion() {
              |		CompletionTester con = new CompletionTester();
              |     String str = new List<Map<String, Set<Integer>>>(<CARET>
              | }
              |}
            """.stripMargin
        val result = findCaretScope(text, "testFindCaretScope3").futureValue
        result match {
            case Some(FindCaretScopeResult(Some(CaretScope(_, Some(typeDefinition))), _)) =>
                assertResult(Some("List<Map<String,Set<Integer>>>"), "Wrong caret type detected.")(typeDefinition.getValueType.map(_.toString))
            case _ =>
                assert(false, "Failed to identify caret type")
        }
    }

    test("testFindCaretScope: `String.`") {
        val text =
            """
              |class CompletionTester {
              | public void testCompletion() {
              |     String.<CARET>
              |     CompletionTester con = new CompletionTester();
              | }
              |}
            """.stripMargin
        val result = findCaretScope(text, "ignored", loadStdLib = true).futureValue
        result match {
            case Some(FindCaretScopeResult(Some(CaretScope(_, Some(typeDefinition))), _)) =>
                assertResult(Some(QualifiedName(Array("System", "String"))), "Wrong caret type detected. Expected 'String'")(typeDefinition.getValueType.map(_.qualifiedName))
            case _ =>
                assert(false, "Failed to identify caret type. Expected 'String'")
        }

    }

    test("testFindCaretScope: `String.;`") {
        val text =
            """
              |class CompletionTester {
              | public void testCompletion() {
              |     String.<CARET>;
              |     CompletionTester con = new CompletionTester();
              | }
              |}
            """.stripMargin
        val result = findCaretScope(text, "ignored", loadStdLib = true).futureValue
        result match {
            case Some(FindCaretScopeResult(Some(CaretScope(_, Some(typeDefinition))), _)) =>
                assertResult(Some(QualifiedName(Array("System", "String"))), "Wrong caret type detected. Expected 'String'")(typeDefinition.getValueType.map(_.qualifiedName))
            case _ =>
                assert(false, "Failed to identify caret type. Expected 'String'")
        }

    }
    test("testFindCaretScope: `String.abbr`") {
        val text =
            """
              |class CompletionTester {
              | public void testCompletion() {
              |     String.abbr<CARET>
              |     CompletionTester con = new CompletionTester();
              | }
              |}
            """.stripMargin
        val result = findCaretScope(text, "ignored", loadStdLib = true).futureValue
        result match {
            case Some(FindCaretScopeResult(Some(CaretScope(_, Some(typeDefinition))), _)) =>
                assertResult(Some(QualifiedName(Array("System", "String"))), "Wrong caret type detected. Expected 'String'")(typeDefinition.getValueType.map(_.qualifiedName))
            case _ =>
                assert(false, "Failed to identify caret type. Expected 'String'")
        }

    }

    test("testFindCaretScope: `str.length().`") {
        val text =
            """
              |class CompletionTester {
              | public void testCompletion() {
              |     String str;
              |     str.length().<CARET>
              | }
              |}
            """.stripMargin
        val result = findCaretScope(text, "ignored", loadStdLib = true).futureValue
        result match {
            case Some(FindCaretScopeResult(Some(CaretScope(_, Some(typeDefinition))), _)) =>
                assert(typeDefinition.getValueType.isDefined, "Value type not found")
                val valueType = typeDefinition.getValueType.get
                assert(QualifiedName(Array("System", "Integer")).couldBeMatch(valueType.qualifiedName), "Expected Integer, actual: " + valueType.qualifiedName)
            case _ =>
                assert(false, "Failed to identify caret type. Expected 'String'")
        }

    }

    test("testFindCaretScope: `str.abbreviate(10).length().`") {
        val text =
            """
              |class CompletionTester {
              | public void testCompletion() {
              |     String str;
              |     str.abbreviate(10).length().<CARET>
              | }
              |}
            """.stripMargin
        val result = findCaretScope(text, "ignored", loadStdLib = true).futureValue
        result match {
            case Some(FindCaretScopeResult(Some(CaretScope(_, Some(typeDefinition))), _)) =>
                assert(typeDefinition.getValueType.isDefined, "Value type not found")
                val valueType = typeDefinition.getValueType.get
                assert(QualifiedName(Array("System", "Integer")).couldBeMatch(valueType.qualifiedName), "Expected Integer, actual: " + valueType.qualifiedName)
            case _ =>
                assert(false, "Failed to identify caret type. Expected 'Integer'")
        }

    }

    test("testFindCaretScope: String literal - `''.<CARET>`") {
        val text =
            """
              |class CompletionTester {
              | public void testCompletion() {
              |     ''.<CARET>
              | }
              |}
            """.stripMargin
        val result = findCaretScope(text, "ignored", loadStdLib = true).futureValue
        result match {
            case Some(FindCaretScopeResult(Some(CaretScope(_, Some(typeDefinition))), _)) =>
                assert(typeDefinition.getValueType.isDefined, "Value type not found")
                val valueType = typeDefinition.getValueType.get
                assert(QualifiedName(Array("System", "String")).couldBeMatch(valueType.qualifiedName), "Expected String, actual: " + valueType.qualifiedName)
            case _ =>
                assert(false, "Failed to identify caret type. Expected 'String'")
        }
    }

    test("testFindCaretScope: Decimal literal - `1.23.<CARET>`") {
        val text =
            """
              |class CompletionTester {
              | public void testCompletion() {
              |     1.23.<CARET>
              | }
              |}
            """.stripMargin
        val result = findCaretScope(text, "ignored", loadStdLib = true).futureValue
        result match {
            case Some(FindCaretScopeResult(Some(CaretScope(_, Some(typeDefinition))), _)) =>
                assert(typeDefinition.getValueType.isDefined, "Value type not found")
                val valueType = typeDefinition.getValueType.get
                assert(QualifiedName(Array("System", "Decimal")).couldBeMatch(valueType.qualifiedName), "Expected Decimal, actual: " + valueType.qualifiedName)
            case _ =>
                assert(false, "Failed to identify caret type. Expected 'Decimal'")
        }
    }

    test("testFindCaretScope: Integer literal - `123.<CARET>`") {
        val text =
            """
              |class CompletionTester {
              | public void testCompletion() {
              |     123.<CARET>
              | }
              |}
            """.stripMargin
        val result = findCaretScope(text, "ignored", loadStdLib = true).futureValue
        result match {
            case Some(FindCaretScopeResult(Some(CaretScope(_, Some(typeDefinition))), _)) =>
                assert(typeDefinition.getValueType.isDefined, "Value type not found")
                val valueType = typeDefinition.getValueType.get
                assert(QualifiedName(Array("System", "Integer")).couldBeMatch(valueType.qualifiedName), "Expected Integer, actual: " + valueType.qualifiedName)
            case _ =>
                assert(false, "Failed to identify caret type. Expected 'Integer'")
        }
    }

    test("testFindCaretScope: Long literal - `123L.<CARET>`") {
        val text =
            """
              |class CompletionTester {
              | public void testCompletion() {
              |     123L.<CARET>
              | }
              |}
            """.stripMargin
        val result = findCaretScope(text, "ignored", loadStdLib = true).futureValue
        result match {
            case Some(FindCaretScopeResult(Some(CaretScope(_, Some(typeDefinition))), _)) =>
                assert(typeDefinition.getValueType.isDefined, "Value type not found")
                val valueType = typeDefinition.getValueType.get
                assert(QualifiedName(Array("System", "Long")).couldBeMatch(valueType.qualifiedName), "Expected Long, actual: " + valueType.qualifiedName)
            case _ =>
                assert(false, "Failed to identify caret type. Expected 'Long'")
        }
    }

    test("testFindCaretScope: Long literal methods - `123L.intValue().<CARET>`") {
        val text =
            """
              |class CompletionTester {
              | public void testCompletion() {
              |     123L.intValue().<CARET>
              | }
              |}
            """.stripMargin
        val result = findCaretScope(text, "ignored", loadStdLib = true).futureValue
        result match {
            case Some(FindCaretScopeResult(Some(CaretScope(_, Some(typeDefinition))), _)) =>
                assert(typeDefinition.getValueType.isDefined, "Value type not found")
                val valueType = typeDefinition.getValueType.get
                assert(QualifiedName(Array("System", "Integer")).couldBeMatch(valueType.qualifiedName), "Expected Integer, actual: " + valueType.qualifiedName)
            case _ =>
                assert(false, "Failed to identify caret type. Expected 'Integer'")
        }
    }

    test("testFindCaretScope: enum values - `SEASONS.<CARET>`") {
        val text =
            """
              |class CompletionTester {
              | enum SEASONS {Spring, Winter, Summer, Autumn}
              | public void testCompletion() {
              |     SEASONS.<CARET>
              | }
              |}
            """.stripMargin
        val result = findCaretScope(text, "testFindCaretScope").futureValue
        result match {
            case Some(FindCaretScopeResult(Some(CaretScope(_, Some(typeDefinition))), _)) =>
                assertResult(Some("SEASONS"), "Wrong caret type detected.")(typeDefinition.getValueType.map(_.qualifiedName.getLastComponent))
            case _ =>
                assert(false, "Failed to identify caret type. Expected 'Opportunity'")
        }
    }

    test("testFindCaretScope: enum values - `SEASONS.Summer.<CARET>`") {
        val text =
            """
              |class CompletionTester {
              | enum SEASONS {Spring, Winter, Summer, Autumn}
              | public void testCompletion() {
              |     SEASONS.Summer.<CARET>
              | }
              |}
            """.stripMargin
        val result = findCaretScope(text, "testFindCaretScope").futureValue
        result match {
            case Some(FindCaretScopeResult(Some(CaretScope(_, Some(typeDefinition))), _)) =>
                assertResult(Some("Summer"), "Wrong caret type detected.")(typeDefinition.getValueType.map(_.qualifiedName.getLastComponent))
            case _ =>
                assert(false, "Failed to identify caret type. Expected 'Opportunity'")
        }
    }

    test("testFindCaretScope: enum values - `SEASONS.Summer.ordinal().<CARET>`") {
        val text =
            """
              |class CompletionTester {
              | enum SEASONS {Spring, Winter, Summer, Autumn}
              | public void testCompletion() {
              |     SEASONS.Summer.ordinal().<CARET>
              | }
              |}
            """.stripMargin
        val result = findCaretScope(text, "testFindCaretScope").futureValue
        result match {
            case Some(FindCaretScopeResult(Some(CaretScope(_, Some(typeDefinition))), _)) =>
                assertResult(Some("Integer"), "Wrong caret type detected.")(typeDefinition.getValueType.map(_.qualifiedName.getLastComponent))
            case _ =>
                assert(false, "Failed to identify caret type. Expected 'Integer'")
        }
    }


    test("testCollectCandidates") {

    }

    test("testListCompletions") {

    }

    private def findCaretScope(text: String, documentName: String, loadStdLib: Boolean = false): Future[Option[FindCaretScopeResult]] = {
        val project = Project(projectPath)
        if (loadStdLib) {
            project.getStandardLibrary // force loading of StandardLibrary
        }
        val caretInDocument = getCaret(text, Paths.get(documentName))
//        val document = caretInDocument.document
        val scopeFinder = new CaretScopeFinder(project)
        scopeFinder.findCaretScope(caretInDocument)
    }

    private def getCaret(text: String, file: Path): CaretInDocument = {
        val caret = CaretUtils.getCaret(text, file)
        caret
    }

}

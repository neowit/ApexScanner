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

import com.neowit.apexscanner.nodes.{MethodBodyNodeType, Position}
import com.neowit.apexscanner.{Project, TestConfigProvider, TextBasedDocument}
import org.scalatest.FunSuite
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
/**
  * Created by Andrey Gavrikov 
  */
class CompletionFinderTest extends FunSuite with TestConfigProvider with ScalaFutures with IntegrationPatience {
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
              |		CompletionTester con = new CompletionTester();
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

    test("testFindCaretScope: `new Object__c(<CARET>`") {
        val text =
            """
              |class CompletionTester {
              | public void testCompletion() {
              |		CompletionTester con = new CompletionTester();
              |     new Object__c(<CARET>
              | }
              |}
            """.stripMargin
        val result = findCaretScope(text, "testFindCaretScope").futureValue
        result match {
            case Some(FindCaretScopeResult(Some(CaretScope(_, Some(typeDefinition))), _)) =>
                assertResult(Some("Object__c"), "Wrong caret type detected.")(typeDefinition.getValueType.map(_.toString))
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



    test("testCollectCandidates") {

    }

    test("testListCompletions") {

    }

    private def findCaretScope(text: String, documentName: String): Future[Option[FindCaretScopeResult]] = {
        val completionFinder = new CompletionFinder(project)
        val caretInDocument = getCaret(text, Paths.get(documentName))
        val document = caretInDocument.document
        val parser = completionFinder.createParser(document)

        completionFinder.findCaretScope(caretInDocument, parser)
    }

    private def getCaret(text: String, file: Path): CaretInDocument = {
        val caretTag = "<CARET>"
        val lines = text.split("""\n""")
        var lineNum = 1
        for (line <- lines) {
            val caretCharacterInLine = line.indexOf(caretTag)
            if (caretCharacterInLine >=0) {
                // found relevant line, remove caret tag from it
                val documentText = text.replace(caretTag, "")
                val fixedDocument = TextBasedDocument(documentText, file)
                return new CaretInDocument(Position(lineNum, caretCharacterInLine), fixedDocument)
            }
            lineNum += 1
        }
        throw new IllegalArgumentException(s"Caret not found in text $text")
    }

}

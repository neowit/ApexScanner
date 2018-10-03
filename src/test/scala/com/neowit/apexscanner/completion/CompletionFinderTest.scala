/*
 *
 *  * Copyright (c) 2017 Andrey Gavrikov.
 *  * this file is part of tooling-force.com application
 *  * https://github.com/neowit/tooling-force.com
 *  *
 *  * This program is free software: you can redistribute it and/or modify
 *  * it under the terms of the GNU Lesser General Public License as published by
 *  * the Free Software Foundation, either version 3 of the License, or
 *  * (at your option) any later version.
 *  *
 *  * This program is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  * GNU Lesser General Public License for more details.
 *  *
 *  * You should have received a copy of the GNU Lesser General Public License
 *  * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.neowit.apexscanner.completion

import java.nio.file.{FileSystems, Paths}

import com.neowit.apexscanner.antlr.CaretUtils
import com.neowit.apexscanner.scanner.actions.{ActionContext, ListCompletionsActionType}
import com.neowit.apexscanner.{Project, TestConfigProvider}
import com.neowit.apexscanner.symbols.Symbol
import org.scalatest.FunSuite
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}

import scala.util.Random


/**
  * Created by Andrey Gavrikov 
  */
class CompletionFinderTest extends FunSuite with TestConfigProvider with ScalaFutures with IntegrationPatience {
private val filePath = getProperty("CompletionFinderTest.path")
    private val projectPath = FileSystems.getDefault.getPath(filePath)

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    var _projectWithStdLib: Option[Project] = None
    def listCompletions(text: String, documentName: String = "test", loadStdLib: Boolean = false): scala.Seq[Symbol] = {
        val project =
            if (loadStdLib) {
                _projectWithStdLib match {
                    case Some(_project) =>
                        // re-use previously loaded project because loading StdLib takes a lot of time
                        _project
                    case None =>
                        val _project = Project(projectPath)
                        _project.loadStdLib() // force loading of StandardLibrary
                        _projectWithStdLib = Option(_project)
                        _project
                }

            } else {
                Project(projectPath)
            }
        val caretInDocument = CaretUtils.getCaret(text, Paths.get(documentName))
        val context = ActionContext("CompletionFinderTest-" + Random.nextString(5), ListCompletionsActionType)
        val completionFinder = new CompletionFinder(project)
        completionFinder.listCompletions(caretInDocument, context)
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    test("testListCompletions: enum values - `SEASONS.<CARET>`") {
        val text =
            """
              |class CompletionTester {
              | enum SEASONS {Spring, Winter, Summer, Autumn}
              | public void testCompletion() {
              |     SEASONS.<CARET>
              | }
              |}
            """.stripMargin
        val result = listCompletions(text)
        result match {
            case symbols if symbols.nonEmpty =>
                assert(symbols.length >= 4, "Result contains less items than expected")
                assert(symbols.exists(_.symbolName == "Spring"), "Missing expected symbol")
            case _ =>
                fail("Expected non empty result")
        }
    }

    test("testListCompletions: enum values - `SEASONS ss = SEASONS.<CARET>`") {
        val text =
            """
              |class CompletionTester {
              | enum SEASONS {Spring, Winter, Summer, Autumn}
              | public void testCompletion() {
              |     SEASONS ss = SEASONS.<CARET>
              | }
              |}
            """.stripMargin
        val result = listCompletions(text)
        result match {
            case symbols if symbols.nonEmpty =>
                assert(symbols.length >= 4, "Result contains less items than expected")
                assert(symbols.exists(_.symbolName == "Spring"), "Missing expected symbol")
            case _ =>
                fail("Expected non empty result")
        }
    }

    test("testListCompletions: enum values - `SEASONS ss = SEASONS.Autumn.<CARET>`") {
        val text =
            """
              |class CompletionTester {
              | enum SEASONS {Spring, Winter, Summer, Autumn}
              | public void testCompletion() {
              |     SEASONS ss = SEASONS.Autumn.<CARET>
              | }
              |}
            """.stripMargin
        val result = listCompletions(text)
        result match {
            case symbols if symbols.nonEmpty =>
                assert(symbols.length >= 2, "Result contains less items than expected")
                assert(symbols.exists(_.symbolName == "name"), "Missing expected symbol: name()")
                assert(symbols.exists(_.symbolName == "ordinal"), "Missing expected symbol: ordinal()")
                assert(symbols.exists(_.symbolName == "equals"), "Missing expected symbol: equals()")
            case _ =>
                fail("Expected non empty result")
        }
    }

    test("testListCompletions: enum values - `SEASONS.Summer.<CARET>`") {
        val text =
            """
              |class CompletionTester {
              | enum SEASONS {Spring, Winter, Summer, Autumn}
              | public void testCompletion() {
              |     SEASONS.Summer.<CARET>
              | }
              |}
            """.stripMargin
        val result = listCompletions(text)
        result match {
            case symbols if symbols.nonEmpty =>
                assert(symbols.length >= 2, "Result contains less items than expected")
                assert(symbols.exists(_.symbolName == "name"), "Missing expected symbol: name()")
                assert(symbols.exists(_.symbolName == "ordinal"), "Missing expected symbol: ordinal()")
                assert(symbols.exists(_.symbolName == "equals"), "Missing expected symbol: equals()")
            case _ =>
                fail("Expected non empty result")
        }
    }

    test("testListCompletions: enhanced for - `for (String str: strings)`") {
        val text =
            """
              |class CompletionTester {
              |
              | public void testCompletion() {
              |     for (String str: strings) {
              |         str.<CARET>
              |     }
              | }
              |}
            """.stripMargin
        val result = listCompletions(text, "", loadStdLib = true)
        result match {
            case symbols if symbols.nonEmpty =>
                assert(symbols.length >= 2, "Result contains less items than expected")
                assert(symbols.exists(_.symbolName == "center"), "Missing expected symbol: name()")
                assert(symbols.exists(_.symbolName == "capitalize"), "Missing expected symbol: ordinal()")
                assert(symbols.exists(_.symbolName == "equals"), "Missing expected symbol: equals()")
            case _ =>
                fail("Expected non empty result")
        }
    }

    test("testListCompletions: enhanced for - `for (String str: stringsByKey.<CARET>)`") {
        val text =
            """
              |class CompletionTester {
              |
              | public void testCompletion() {
              |     Map<String, List<String>> stringsByKey;
              |     for (String str: stringsByKey.<CARET>) {
              |     }
              | }
              |}
            """.stripMargin
        val result = listCompletions(text, "", loadStdLib = true)
        result match {
            case symbols if symbols.nonEmpty =>
                assert(symbols.length >= 2, "Result contains less items than expected")
                assert(symbols.exists(_.symbolName == "get"), "Missing expected symbol: name()")
                assert(symbols.exists(_.symbolName == "containsKey"), "Missing expected symbol: ordinal()")
                assert(symbols.exists(_.symbolName == "keySet"), "Missing expected symbol: equals()")
            case _ =>
                fail("Expected non empty result")
        }
    }

    test("testListCompletions: for - `for (Integer i = 0; i < 10; i++)`") {
        val text =
            """
              |class CompletionTester {
              |
              | public void testCompletion() {
              |     for (Integer i = 0; i < 10; i++) {
              |         i.<CARET>
              |     }
              | }
              |}
            """.stripMargin
        val result = listCompletions(text, "", loadStdLib = true)
        result match {
            case symbols if symbols.nonEmpty =>
                assert(symbols.length >= 2, "Result contains less items than expected")
                assert(symbols.exists(_.symbolName == "format"), "Missing expected symbol: name()")
                assert(symbols.exists(_.symbolName == "valueOf"), "Missing expected symbol: ordinal()")
            case _ =>
                fail("Expected non empty result")
        }
    }

    test("testListCompletions: for - `for (Integer i = 0; i < lst.<CARET>; i++)`") {
        val text =
            """
              |class CompletionTester {
              |
              | public void testCompletion() {
              |     List<String> lst;
              |     for (Integer i = 0; i < lst.<CARET>; i++) {
              |     }
              | }
              |}
            """.stripMargin
        val result = listCompletions(text, "", loadStdLib = true)
        result match {
            case symbols if symbols.nonEmpty =>
                assert(symbols.length >= 2, "Result contains less items than expected")
                assert(symbols.exists(_.symbolName == "get"), "Missing expected symbol: name()")
                assert(symbols.exists(_.symbolName == "clear"), "Missing expected symbol: ordinal()")
                assert(symbols.exists(_.symbolName == "addAll"), "Missing expected symbol: equals()")
            case _ =>
                fail("Expected non empty result")
        }
    }

    test("testListCompletions: `String.is<CARET>`") {
        val text =
            """
              |class CompletionTester {
              |
              | public void testCompletion() {
              |     String.is<CARET>
              | }
              |}
            """.stripMargin
        val result = listCompletions(text, "", loadStdLib = true)
        result match {
            case symbols if symbols.nonEmpty =>
                assert(symbols.length >= 2, "Result contains less items than expected")
                assert(symbols.exists(_.symbolName == "isEmpty"), "Missing expected symbol")
                assert(symbols.exists(_.symbolName == "isNotBlank"), "Missing expected symbol")
                assert(symbols.exists(_.symbolName == "isNotEmpty"), "Missing expected symbol")
            case _ =>
                fail("Expected non empty result")
        }
    }

    test("testListCompletions: `Test.<CARET>`") {
        val text =
            """
              |class CompletionTester {
              |
              | public void testCompletion() {
              |     Test.<CARET>
              | }
              |}
            """.stripMargin
        val result = listCompletions(text, "", loadStdLib = true)
        result match {
            case symbols if symbols.nonEmpty =>
                assert(symbols.length >= 2, "Result contains less items than expected")
                assert(symbols.exists(_.symbolName == "isRunningTest"), "Missing expected symbol")
                assert(symbols.exists(_.symbolName == "startTest"), "Missing expected symbol")
                assert(symbols.exists(_.symbolName == "stopTest"), "Missing expected symbol")
            case _ =>
                fail("Expected non empty result")
        }
    }
    test("testListCompletions: `Test.<CARET>` NEW_LINE String Str; ") {
        val text =
            """
              |class CompletionTester {
              |
              | public void testCompletion() {
              |     Test.<CARET>
              |     String str;
              | }
              |}
            """.stripMargin
        val result = listCompletions(text, "", loadStdLib = true)
        result match {
            case symbols if symbols.nonEmpty =>
                assert(symbols.length >= 2, "Result contains less items than expected")
                assert(symbols.exists(_.symbolName == "isRunningTest"), "Missing expected symbol")
                assert(symbols.exists(_.symbolName == "startTest"), "Missing expected symbol")
                assert(symbols.exists(_.symbolName == "stopTest"), "Missing expected symbol")
            case _ =>
                fail("Expected non empty result")
        }
    }
    test("testListCompletions: `System.<CARET>`") {
        val text =
            """
              |class CompletionTester {
              |
              | public void testCompletion() {
              |     System.<CARET>
              | }
              |}
            """.stripMargin
        val result = listCompletions(text, "", loadStdLib = true)
        result match {
            case symbols if symbols.nonEmpty =>
                assert(symbols.length >= 2, "Result contains less items than expected")
                assert(symbols.exists(_.symbolName == "debug"), "Missing expected symbol")
                assert(symbols.exists(_.symbolName == "now"), "Missing expected symbol")
                assert(symbols.exists(_.symbolName == "today"), "Missing expected symbol")
                assert(symbols.exists(_.symbolName == "assertEquals"), "Missing expected symbol")
            case _ =>
                fail("Expected non empty result")
        }
    }

    test("testListCompletions: `Database.<CARET>`") {
        val text =
            """
              |class CompletionTester {
              |
              | public void testCompletion() {
              |     Database.<CARET>
              | }
              |}
            """.stripMargin
        val result = listCompletions(text, "", loadStdLib = true)
        result match {
            case symbols if symbols.nonEmpty =>
                assert(symbols.length >= 2, "Result contains less items than expected")
                assert(symbols.exists(_.symbolName == "update"), "Missing expected symbol")
                assert(symbols.exists(_.symbolName == "upsert"), "Missing expected symbol")
                assert(symbols.exists(_.symbolName == "delete"), "Missing expected symbol")
                assert(symbols.exists(_.symbolName == "undelete"), "Missing expected symbol")
            case _ =>
                fail("Expected non empty result")
        }
    }

    test("testListCompletions: `Site.<CARET>`") {
        val text =
            """
              |class CompletionTester {
              |
              | public void testCompletion() {
              |     Site.<CARET>
              | }
              |}
            """.stripMargin
        val result = listCompletions(text, "", loadStdLib = true)
        result match {
            case symbols if symbols.nonEmpty =>
                assert(symbols.length >= 2, "Result contains less items than expected")
                assert(symbols.exists(_.symbolName == "getSiteType"), "Missing expected symbol")
                assert(symbols.exists(_.symbolName == "getPathPrefix"), "Missing expected symbol")
                assert(symbols.exists(_.symbolName == "getMasterLabel"), "Missing expected symbol")
                assert(symbols.exists(_.symbolName == "getSiteId"), "Missing expected symbol")
            case _ =>
                fail("Expected non empty result")
        }
    }

    test("testListCompletions: `ApexPages.currentPage().<CARET>`") {
        val text =
            """
              |class CompletionTester {
              |
              | public void testCompletion() {
              |     ApexPages.currentPage().<CARET>
              | }
              |}
            """.stripMargin
        val result = listCompletions(text, "", loadStdLib = true)
        result match {
            case symbols if symbols.nonEmpty =>
                assert(symbols.lengthCompare(2) >= 0, "Result contains less items than expected")
                assert(symbols.exists(_.symbolName == "getParameters"), "Missing expected symbol")
            case _ =>
                fail("Expected non empty result")
        }
    }

    test("testListCompletions: Annotation name `@<CARET>`") {
        val text =
            """
              |class CompletionTester {
              | @<CARET>
              |}
            """.stripMargin
        val result = listCompletions(text, "", loadStdLib = true)
        result match {
            case symbols if symbols.nonEmpty =>
                assert(symbols.lengthCompare(10) >= 0, "Result contains less items than expected")
                assert(symbols.exists(_.symbolName == "AuraEnabled"), "Missing expected symbol")
                assert(symbols.exists(_.symbolName == "Future"), "Missing expected symbol")
            case _ =>
                fail("Expected non empty result")
        }
    }

    test("testListCompletions: `collection variable declaration with assignment - no ';'`") {
        // in this test - adding ';' after <CARET> fixes the issue
        val text =
            """
              |class CompletionTester {
              | static String str;
              | public void testCompletion() {
              |     List<String> strs = CompletionTester.<CARET>
              | }
              |}
            """.stripMargin
        val result = listCompletions(text, "", loadStdLib = true)
        result match {
            case symbols if symbols.nonEmpty =>
                assert(symbols.lengthCompare(1) >= 0, "Result contains less items than expected")
                assert(symbols.exists(_.symbolName == "str"), "Missing expected symbol")
            case _ =>
                fail("Expected non empty result")
        }
    }

    test("testListCompletions: `assignment to variable - no ';'`") {
        // in this test - adding ';' after <CARET> fixes the issue
        val text =
            """
              |class CompletionTester {
              | static String str;
              | public void testCompletion() {
              |     str = CompletionTester.<CARET>
              | }
              |}
            """.stripMargin
        val result = listCompletions(text, "", loadStdLib = true)
        result match {
            case symbols if symbols.nonEmpty =>
                assert(symbols.lengthCompare(1) >= 0, "Result contains less items than expected")
                assert(symbols.exists(_.symbolName == "str"), "Missing expected symbol")
            case _ =>
                fail("Expected non empty result")
        }
    }
}

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
import com.neowit.apexscanner.{Project, TestConfigProvider}
import com.neowit.apexscanner.symbols.Symbol
import org.scalatest.FunSuite
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by Andrey Gavrikov 
  */
class CompletionFinderTest extends FunSuite with TestConfigProvider with ScalaFutures with IntegrationPatience {
private val filePath = getProperty("CompletionFinderTest.path")
    private val projectPath = FileSystems.getDefault.getPath(filePath)

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
        val result = listCompletions(text).futureValue
        result match {
            case symbols if symbols.nonEmpty =>
                assert(symbols.length >= 4, "Result contains ,ess items than expected")
                assert(symbols.exists(_.symbolName == "Spring"), "Missing expected symbol")
            case _ =>
                assert(false, "Expected non empty result")
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
        val result = listCompletions(text).futureValue
        result match {
            case symbols if symbols.nonEmpty =>
                assert(symbols.length >= 2, "Result contains ,ess items than expected")
                assert(symbols.exists(_.symbolName == "name"), "Missing expected symbol: name()")
                assert(symbols.exists(_.symbolName == "ordinal"), "Missing expected symbol: ordinal()")
                assert(symbols.exists(_.symbolName == "equals"), "Missing expected symbol: equals()")
            case _ =>
                assert(false, "Expected non empty result")
        }
    }

    var _projectWithStdLib: Option[Project] = None
    def listCompletions(text: String, documentName: String = "test", loadStdLib: Boolean = false): Future[scala.Seq[Symbol]] = {
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

        val completionFinder = new CompletionFinder(project)
        completionFinder.listCompletions(caretInDocument)
    }
}

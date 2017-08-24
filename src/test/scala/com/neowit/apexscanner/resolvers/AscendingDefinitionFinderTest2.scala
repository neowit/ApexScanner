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

package com.neowit.apexscanner.resolvers

import java.nio.file.{Path, Paths}

import com.neowit.apexscanner.Project
import com.neowit.apexscanner.antlr.CaretUtils
import com.neowit.apexscanner.ast.QualifiedName
import com.neowit.apexscanner.nodes.{AstNode, IsTypeDefinition}
import org.scalatest.FunSuite

/**
  * Created by Andrey Gavrikov 
  */
class AscendingDefinitionFinderTest2 extends FunSuite {
    //implicit override val patienceConfig: PatienceConfig = PatienceConfig(timeout = Span(2, Seconds), interval = Span(20, Millis))

    // for cases when we do not really care about project path - get something random
    private val projectPath: Path = Paths.get(System.getProperty("java.io.tmpdir"))

    test("findDefinition: `method1(valByKey);`") {
        val text =
            """
              |class CompletionTester {
              | private void test() {
              |     final Map<String, Integer> valByKey;
              |     meth<CARET>od1(valByKey);
              | }
              |
              | public void method1(final Map<String, Integer> objByKey) {
              | }
              |}
            """.stripMargin
        //val resultNodes = findDefinition(text).futureValue
        val resultNodes = findDefinition(text)
        assert(resultNodes.nonEmpty, "Expected to find non empty result")
        assertResult(1,"Wrong number of results found") (resultNodes.length)
        resultNodes.head match {
            case typeDefinition: IsTypeDefinition =>
                assertResult(Option(QualifiedName(Array("CompletionTester", "method1"))), "Wrong caret type detected. Expected 'method1()'")(typeDefinition.qualifiedName)
            case _ =>
                fail("Failed to locate correct node. Expected method1()")
        }

    }

    test("findDefinition: `result = method1(valByKey);`") {
        val text =
            """
              |class CompletionTester {
              | private void test() {
              |     final Map<String, Integer> valByKey;
              |     result = meth<CARET>od1(valByKey);
              | }
              |
              | public void method1(final Map<String, Integer> objByKey) {
              | }
              |}
            """.stripMargin
        //val resultNodes = findDefinition(text).futureValue
        val resultNodes = findDefinition(text)
        assert(resultNodes.nonEmpty, "Expected to find non empty result")
        assertResult(1,"Wrong number of results found") (resultNodes.length)
        resultNodes.head match {
            case typeDefinition: IsTypeDefinition =>
                assertResult(Option(QualifiedName(Array("CompletionTester", "method1"))), "Wrong caret type detected. Expected 'method1()'")(typeDefinition.qualifiedName)
            case _ =>
                fail( "Failed to locate correct node. Expected method1()")
        }

    }

    test("findDefinition: `method parameter`") {
        val text =
            """
              |class CompletionTester {
              | public void method1(final Integer param1) {
              |     some = para<CARET>m1;
              | }
              |}
            """.stripMargin
        //val resultNodes = findDefinition(text).futureValue
        val resultNodes = findDefinition(text)
        assert(resultNodes.nonEmpty, "Expected to find non empty result")
        assertResult(1,"Wrong number of results found") (resultNodes.length)
        resultNodes.head match {
            case typeDefinition: IsTypeDefinition =>
                assertResult(Option(QualifiedName(Array("param1"))), "Wrong caret type detected. Expected 'param1'")(typeDefinition.qualifiedName)
                assertResult(Option(QualifiedName(Array("Integer"))), "Wrong caret type detected. Expected 'Integer'")(typeDefinition.getValueType.map(_.qualifiedName))
            case _ =>
                fail( "Failed to locate correct node. Expected method1()")
        }

    }

    test("findDefinition: `method parameter when class variable with same name exists`") {
        val text =
            """
              |class CompletionTester {
              | String param1;
              | public void method1(final Integer param1) {
              |     some = para<CARET>m1;
              | }
              |}
            """.stripMargin
        //val resultNodes = findDefinition(text).futureValue
        val resultNodes = findDefinition(text)
        assert(resultNodes.nonEmpty, "Expected to find non empty result")
        assertResult(1,"Wrong number of results found") (resultNodes.length)
        resultNodes.head match {
            case typeDefinition: IsTypeDefinition =>
                assertResult(Option(QualifiedName(Array("param1"))), "Wrong caret type detected. Expected 'param1'")(typeDefinition.qualifiedName)
                assertResult(Option(QualifiedName(Array("Integer"))), "Wrong caret type detected. Expected 'Integer'")(typeDefinition.getValueType.map(_.qualifiedName))
            case _ =>
                fail( "Failed to locate correct node. Expected method1()")
        }

    }

    test("findDefinition: `class variable when method parameter with same name exists`") {
        val text =
            """
              |class CompletionTester {
              | String param1;
              | public void method1(final Integer param1) {
              |     some = this.para<CARET>m1;
              | }
              |}
            """.stripMargin
        //val resultNodes = findDefinition(text).futureValue
        val resultNodes = findDefinition(text)
        assert(resultNodes.nonEmpty, "Expected to find non empty result")
        assertResult(1,"Wrong number of results found") (resultNodes.length)
        resultNodes.head match {
            case typeDefinition: IsTypeDefinition =>
                assertResult(Option(QualifiedName(Array("CompletionTester", "param1"))), "Wrong caret type detected.")(typeDefinition.qualifiedName)
                assertResult(Option(QualifiedName(Array("String"))), "Wrong caret type detected. Class variable")(typeDefinition.getValueType.map(_.qualifiedName))
            case _ =>
                fail( "Failed to locate correct node. Expected method1()")
        }

    }

    test("findDefinition: `apex class property`") {
        val text =
            """
              |class CompletionTester {
              | public String prop1 {get; set;}
              |
              | public void method1(final Integer param1) {
              |     pro<CARET>p1 = '';
              | }
              |}
            """.stripMargin
        //val resultNodes = findDefinition(text).futureValue
        val resultNodes = findDefinition(text)
        assert(resultNodes.nonEmpty, "Expected to find non empty result")
        assertResult(1,"Wrong number of results found") (resultNodes.length)
        resultNodes.head match {
            case typeDefinition: IsTypeDefinition =>
                assertResult(Option(QualifiedName(Array("CompletionTester", "prop1"))), "Wrong caret type detected.")(typeDefinition.qualifiedName)
                assertResult(Option(QualifiedName(Array("String"))), "Wrong caret type detected. Class variable")(typeDefinition.getValueType.map(_.qualifiedName))
            case _ =>
                fail( "Failed to locate correct node. Expected method1()")
        }

    }

    test("findDefinition: `method variable`") {
        val text =
            """
              |class CompletionTester {
              | public void findLocalVariableType(String str) {
              |     Integer int = 0;
              |     methodWith2Params(in<CARET>t, str);
              | }
              |}
            """.stripMargin
        //val resultNodes = findDefinition(text).futureValue
        val resultNodes = findDefinition(text)
        assert(resultNodes.nonEmpty, "Expected to find non empty result")
        assertResult(1,"Wrong number of results found") (resultNodes.length)
        resultNodes.head match {
            case typeDefinition: IsTypeDefinition =>
                assertResult(Option(QualifiedName(Array("int"))), "Wrong caret type detected.")(typeDefinition.qualifiedName)
                assertResult(Option(QualifiedName(Array("Integer"))), "Wrong caret type detected.")(typeDefinition.getValueType.map(_.qualifiedName))
            case _ =>
                fail( "Failed to locate correct node. Expected method1()")
        }
    }

    test("findDefinition: `method1('some' + str1 + ' other ' + int1)`") {
        val text =
            """
              |class CompletionTester {
              | String str1;
              | Integer int1;
              | // find definition of method1(string)
              | met<CARET>hod1('some' + str1 + ' other ' + int1);
              |
              | public void method1(String str) {
              | }
              |}
            """.stripMargin
        //val resultNodes = findDefinition(text).futureValue
        val resultNodes = findDefinition(text, "TestDocument.cls", loadStdLib = true)
        assert(resultNodes.nonEmpty, "Expected to find non empty result")
        assertResult(1,"Wrong number of results found") (resultNodes.length)
        resultNodes.head match {
            case typeDefinition: IsTypeDefinition =>
                assertResult(Option(QualifiedName(Array("CompletionTester", "method1"))), "Wrong caret type detected.")(typeDefinition.qualifiedName)
                assertResult(Option(QualifiedName(Array("void"))), "Wrong caret type detected.")(typeDefinition.getValueType.map(_.qualifiedName))
            case _ =>
                fail( "Failed to locate correct node. Expected method1()")
        }
    }

    test("findDefinition: `InnerCla<CARET>ss var`") {
        val text =
            """
              |class CompletionTester {
              | InnerCla<CARET>ss var;
              |
              | private class InnerClass { }
              |}
            """.stripMargin
        //val resultNodes = findDefinition(text).futureValue
        val resultNodes = findDefinition(text)
        assert(resultNodes.nonEmpty, "Expected to find non empty result")
        assertResult(1,"Wrong number of results found") (resultNodes.length)
        resultNodes.head match {
            case typeDefinition: IsTypeDefinition =>
                assertResult(Option(QualifiedName(Array("CompletionTester", "InnerClass"))), "Wrong caret type detected.")(typeDefinition.qualifiedName)
                assertResult(Option(QualifiedName(Array("CompletionTester", "InnerClass"))), "Wrong caret type detected.")(typeDefinition.getValueType.map(_.qualifiedName))
            case _ =>
                fail( "Failed to locate correct node")
        }
    }

    test("findDefinition: `InnerClass va<CARET>r`") {
        val text =
            """
              |class CompletionTester {
              | InnerClass va<CARET>r;
              |
              | private class InnerClass { }
              |}
            """.stripMargin
        //val resultNodes = findDefinition(text).futureValue
        val resultNodes = findDefinition(text)
        assert(resultNodes.nonEmpty, "Expected to find non empty result")
        assertResult(1,"Wrong number of results found") (resultNodes.length)
        resultNodes.head match {
            case typeDefinition: IsTypeDefinition =>
                assertResult(Option(QualifiedName(Array("CompletionTester", "var"))), "Wrong caret type detected.")(typeDefinition.qualifiedName)
                assertResult(Option(QualifiedName(Array("CompletionTester", "InnerClass"))), "Wrong caret type detected.")(typeDefinition.getValueType.map(_.qualifiedName))
            case _ =>
                fail( "Failed to locate correct node. Expected method1()")
        }
    }

    test("findDefinition: `InnerClass param referencing Enum`") {
        val text =
            """
              |class CompletionTester {
              | Enum TestEnum {One, Two};
              |
              | InnerClass var;
              | var.refEn<CARET>um;
              |
              | private class InnerClass {
              |   TestEnum refEnum;
              | }
              |}
            """.stripMargin
        //val resultNodes = findDefinition(text).futureValue
        val resultNodes = findDefinition(text)
        assert(resultNodes.nonEmpty, "Expected to find non empty result")
        assertResult(1,"Wrong number of results found") (resultNodes.length)
        resultNodes.head match {
            case typeDefinition: IsTypeDefinition =>
                assertResult(Option(QualifiedName(Array("CompletionTester", "InnerClass", "refEnum"))), "Wrong caret type detected.")(typeDefinition.qualifiedName)
                assertResult(Option(QualifiedName(Array("CompletionTester", "TestEnum"))), "Wrong caret type detected.")(typeDefinition.getValueType.map(_.qualifiedName))
            case _ =>
                fail( "Failed to locate correct node. Expected method1()")
        }
    }

    test("findDefinition: `method call with Creator parameter`") {
        val text =
            """
              |class CompletionTester {
              | Map<String, Object> valueByName;
              |
              | meth<CARET>od1('str', new List<String>{}, valueByName);
              |
              | Boolean method1(final String str, final List<Integer> lst, final Map<String, Object> objByStr) {
              | }
              |
              | // expect to find this method
              | Integer method1(final String str, final List<String> lst, final Map<String, Object> objByStr) {
              | }
              |
              |
              |}
            """.stripMargin
        //val resultNodes = findDefinition(text).futureValue
        val resultNodes = findDefinition(text)
        assert(resultNodes.nonEmpty, "Expected to find non empty result")
        assertResult(1,"Wrong number of results found") (resultNodes.length)
        resultNodes.head match {
            case typeDefinition: IsTypeDefinition =>
                assertResult(Option(QualifiedName(Array("CompletionTester", "method1"))), "Wrong caret type detected.")(typeDefinition.qualifiedName)
                assertResult(Option(QualifiedName(Array("Integer"))), "Wrong caret type detected.")(typeDefinition.getValueType.map(_.qualifiedName))
            case _ =>
                fail( "Failed to locate correct node. Expected method1()")
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////
    var _projectWithStdLib: Option[Project] = None
    private def findDefinition(text: String, documentName: String = "test", loadStdLib: Boolean = false): scala.Seq[AstNode] = {
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
        project.getAst(caretInDocument.document) match {
            case Some(result) =>
                val finder = new AscendingDefinitionFinder()
                finder.findDefinition(result.rootNode, caretInDocument.position)
            case _ =>
                Seq.empty
        }
    }
}

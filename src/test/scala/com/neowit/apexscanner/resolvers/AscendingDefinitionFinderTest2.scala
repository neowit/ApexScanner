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

import com.neowit.apexscanner.VirtualDocument.DocumentId
import com.neowit.apexscanner.{Project, TextBasedDocument, VirtualDocument}
import com.neowit.apexscanner.antlr.CaretUtils
import com.neowit.apexscanner.ast.{AstBuilderResult, QualifiedName}
import com.neowit.apexscanner.nodes.{AstNode, IsTypeDefinition}
import com.neowit.apexscanner.scanner.actions.{ActionContext, FindSymbolActionType}
import org.scalatest.FunSuite

import scala.util.Random

/**
  * Created by Andrey Gavrikov
  *
  * tests of AscendingDefinitionFinder in single-file/class scenarios
  */
class AscendingDefinitionFinderTest2 extends FunSuite {
    //implicit override val patienceConfig: PatienceConfig = PatienceConfig(timeout = Span(2, Seconds), interval = Span(20, Millis))

    // for cases when we do not really care about project path - get something random
    private val projectPath: Path = Paths.get(System.getProperty("java.io.tmpdir"))
    private class TestDocument(text: String, fileName: String) extends TextBasedDocument(text, fileOpt = Option(Paths.get(fileName)), offset = None) {
        override def getId: DocumentId = fileName
    }
    ///////////////////////////////////////////////////////////////////////////////////
    var _projectWithStdLib: Option[Project] = None
    private def getProject(loadStdLib: Boolean = false): Project = {
        val project =
            _projectWithStdLib match {
                case Some(_project) =>
                    // re-use previously loaded project because loading StdLib takes a lot of time
                    _project
                case None =>
                    val _project = Project(projectPath)
                    _projectWithStdLib = Option(_project)
                    _project
            }
        if (loadStdLib && !project.getExternalLibraries.exists(_.getName == "StdLib")) {
            project.loadStdLib() // force loading of StandardLibrary
        }

        project
    }

    private def loadDocument(project: Project, document: VirtualDocument): Option[AstBuilderResult] = {
        project.getAst(document, forceRebuild = true)
    }

    private def findDefinition(text: String, documentName: String = "test", loadStdLib: Boolean = false): scala.Seq[AstNode] = {
        loadDocument(getProject(), new TestDocument(text, documentName))
        val project = getProject(loadStdLib)
        val caretInDocument = CaretUtils.getCaret(text, Paths.get(documentName))
        project.getAst(caretInDocument.document, forceRebuild = true) match {
            case Some(result) =>
                val actionContext = ActionContext("AscendingDefinitionFinderTest2-" + Random.nextString(5), FindSymbolActionType)
                val finder = new AscendingDefinitionFinder(actionContext)
                finder.findDefinition(result.rootNode, caretInDocument.position)
            case _ =>
                Seq.empty
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////

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

    test("findDefinition: `method1(123);`") {
        val text =
            """
              |class CompletionTester {
              | private void test() {
              |     meth<CARET>od1(123);
              | }
              |
              | public void method1(final Integer i) {
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

    test("findDefinition: `method1(Enum.constant);` - check that constant is correctly matched to its Enum") {
        val text =
            """
              |class CompletionTester {
              | Enum TestEnum {One, Two};
              |
              | private void test() {
              |     meth<CARET>od1(TestEnum.One);
              | }
              |
              | public void method1(final TestEnum en) {
              | }
              |}
            """.stripMargin
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
                fail( "Failed to locate correct node.")
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
                fail( "Failed to locate correct node.")
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

    test("findDefinition: inside SOQL statement") {
        val text =
            """
              |class CompletionTester {
              | Integer i = [select Name from Acc<CARET>ount];
              |}
            """.stripMargin
        //val resultNodes = findDefinition(text).futureValue
        val resultNodes = findDefinition(text)
        assert(resultNodes.nonEmpty, "Expected to find non empty result")
        assertResult(1,"Wrong number of results found") (resultNodes.length)
        resultNodes.head match {
            case typeDefinition: IsTypeDefinition =>
                assertResult(Option(QualifiedName(Array("Account"))), "Wrong caret type detected.")(typeDefinition.qualifiedName)
                assertResult(Option(QualifiedName(Array("Account"))), "Wrong caret type detected.")(typeDefinition.getValueType.map(_.qualifiedName))
            case _ =>
                fail( "Failed to locate correct node.")
        }
    }

    test("findDefinition: inside SOQL statement: Child Relationship Subquery") {
        val text =
            """
              |class CompletionTester {
              | Integer i = [select Name, (select FirstName from Con<CARET>tacts) from Account];
              |}
            """.stripMargin
        //val resultNodes = findDefinition(text).futureValue
        val resultNodes = findDefinition(text)
        assert(resultNodes.nonEmpty, "Expected to find non empty result")
        assertResult(1,"Wrong number of results found") (resultNodes.length)
        resultNodes.head match {
            case typeDefinition: IsTypeDefinition =>
                assertResult(Option(QualifiedName("Contacts")), "Wrong caret type detected.")(typeDefinition.qualifiedName)
                //typeDefinition.getValueType - can not be tested from this test because requires access to SFDC DB
            case _ =>
                fail( "Failed to locate correct node.")
        }
    }

    test("findDefinition: inside SOQL statement: Subquery with alias") {
        val text =
            """
              |class CompletionTester {
              | Integer i = [select Name, (select FirstName from a.Con<CARET>tacts) from Account a];
              |}
            """.stripMargin
        //val resultNodes = findDefinition(text).futureValue
        val resultNodes = findDefinition(text)
        assert(resultNodes.nonEmpty, "Expected to find non empty result")
        assertResult(1,"Wrong number of results found") (resultNodes.length)
        resultNodes.head match {
            case typeDefinition: IsTypeDefinition =>
                assertResult(Option(QualifiedName(Array("a", "Contacts"))), "Wrong caret type detected.")(typeDefinition.qualifiedName)
                //typeDefinition.getValueType - can not be tested from this test because requires access to SFDC DB
            case _ =>
                fail( "Failed to locate correct node.")
        }
    }

    test("findDefinition: inside SOQL statement: WHERE <CARET>") {
        val text =
            """
              |class CompletionTester {
              | Integer i = [select Id from Account where
              | <CARET>];
              |}
            """.stripMargin
        //val resultNodes = findDefinition(text).futureValue
        val resultNodes = findDefinition(text)
        assert(resultNodes.nonEmpty, "Expected to find non empty result")
        assertResult(1,"Wrong number of results found") (resultNodes.length)
        resultNodes.head match {
            case typeDefinition: IsTypeDefinition =>
                //qualified name in case of SoqlQueryNode is its string content, e.g. "selectIdfromAccountwhere"
                //assertResult(Option(QualifiedName("Account")), "Wrong caret type detected.")(typeDefinition.qualifiedName)
                assertResult(Option(QualifiedName("Account")), "Wrong caret type detected.")(typeDefinition.getValueType.map(_.qualifiedName))
            case _ =>
                fail( "Failed to locate correct node.")
        }
    }

    test("findDefinition: inside SOQL statement: WHERE Name = '' and <CARET>") {
        val text =
            """
              |class CompletionTester {
              | Integer i = [select Id from Account where Name = '' and <CARET>];
              |}
            """.stripMargin
        //val resultNodes = findDefinition(text).futureValue
        val resultNodes = findDefinition(text)
        assert(resultNodes.nonEmpty, "Expected to find non empty result")
        assertResult(1,"Wrong number of results found") (resultNodes.length)
        resultNodes.head match {
            case typeDefinition: IsTypeDefinition =>
                //qualified name in case of SoqlQueryNode is its string content, e.g. "selectIdfromAccountwhereName=''and"
                //assertResult(Option(QualifiedName("Account")), "Wrong caret type detected.")(typeDefinition.qualifiedName)
                assertResult(Option(QualifiedName("Account")), "Wrong caret type detected.")(typeDefinition.getValueType.map(_.qualifiedName))
            case _ =>
                fail( "Failed to locate correct node.")
        }
    }

    test("findDefinition: apex inside SOQL statement: WHERE Name = :som<CARET>eValue") {
        val text =
            """
              |class CompletionTester {
              | String someValue = '';
              | Integer i = [select Id from Account where Name = :som<CARET>eValue];
              |}
            """.stripMargin

        val resultNodes = findDefinition(text)
        assert(resultNodes.nonEmpty, "Expected to find non empty result")
        assertResult(1,"Wrong number of results found") (resultNodes.length)
        resultNodes.head match {
            case typeDefinition: IsTypeDefinition =>
                assertResult(Option(QualifiedName("CompletionTester", "someValue")), "Wrong caret type detected.")(typeDefinition.qualifiedName)
                assertResult(Option(QualifiedName("String")), "Wrong caret type detected.")(typeDefinition.getValueType.map(_.qualifiedName))
            case _ =>
                fail( "Failed to locate correct node.")
        }
    }

    test("findDefinition: apex inside SOQL statement: WHERE Name = :meth<CARET>od1()") {
        val text =
            """
              |class CompletionTester {
              | String method1() {}
              | System.debug([select Id from Account
              |where Name =: meth<CARET>od1()]);
              |}
            """.stripMargin

        val resultNodes = findDefinition(text)
        assert(resultNodes.nonEmpty, "Expected to find non empty result")
        assertResult(1,"Wrong number of results found") (resultNodes.length)
        resultNodes.head match {
            case typeDefinition: IsTypeDefinition =>
                assertResult(Option(QualifiedName("CompletionTester", "method1")), "Wrong caret type detected.")(typeDefinition.qualifiedName)
                assertResult(Option(QualifiedName("String")), "Wrong caret type detected.")(typeDefinition.getValueType.map(_.qualifiedName))
            case _ =>
                fail( "Failed to locate correct node.")
        }
    }

    test("findDefinition: `for ( String str: method1(123)) `") {
        val text =
            """
              |class CompletionTester {
              | private void test() {
              |     for ( String str: met<CARET>hod1(123)) {
              |
              |     }
              | }
              |
              | public List<String> method1(final Integer i) {
              | }
              |}
            """.stripMargin

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

    test("findDefinition: `cls.method1(123) `") {
        val text =
            """
              |class CompletionTester {
              | private void test() {
              |     CompletionTester cls;
              |     cls.met<CARET>hod1(123);
              | }
              |
              | public List<String> method1(final Integer i) {
              | }
              |}
            """.stripMargin

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

    test("findDefinition: `test apex auto conversion Integer to Decimal in method parameters`") {
        val text =
            """
              |class CompletionTester {
              | private void test() {
              |     Integer i = 0;
              |     final Decimal count = getOr<CARET>Else((Decimal)i, 0) + 1; // 0 has type Integer
              | }
              |
              | private static Decimal getOrElse(final Decimal val, final Decimal defaultVal) { } // line 8
              | private static Decimal getOrElse(final Decimal val, final Integer defaultVal) { } // line 9
              |}
            """.stripMargin

        val resultNodes = findDefinition(text, "getOrElse", loadStdLib = true)
        assert(resultNodes.nonEmpty, "Expected to find non empty result")
        assertResult(1,"Wrong number of results found") (resultNodes.length)
        resultNodes.head match {
            case typeDefinition: IsTypeDefinition =>
                assertResult(Option(QualifiedName(Array("CompletionTester", "getOrElse"))), "Wrong caret type detected. Expected 'getOrElse()'")(typeDefinition.qualifiedName)
                assertResult(9, "expected method on line 9")(typeDefinition.range.start.line)
            case _ =>
                fail("Failed to locate correct node")
        }
    }

    test("findDefinition: `test automatic class casting in method parameters - Inheritance`") {
        val text =
            """
              |class CompletionTester {
              | virtual class Base { }
              | virtual class Level1 extends Base { }
              | virtual class Level2 extends Level1 { }
              | private void test() {
              |     Level1 x = new Level2();
              |     meth<CARET>od1(x);
              | }
              |
              | private static void method1(Level2 v) { } // line 11
              | // private static void method1(Level1 v) { } // line 12
              | private static void method1(Base v) { } // line 13
              |}
            """.stripMargin

        val resultNodes = findDefinition(text)
        assert(resultNodes.nonEmpty, "Expected to find non empty result")
        assertResult(1,"Wrong number of results found") (resultNodes.length)
        resultNodes.head match {
            case typeDefinition: IsTypeDefinition =>
                assertResult(Option(QualifiedName(Array("CompletionTester", "method1"))), "Wrong caret type detected. Expected 'method1()'")(typeDefinition.qualifiedName)
                assertResult(13, "expected method on line 13 - Base")(typeDefinition.range.start.line)
            case _ =>
                fail("Failed to locate correct node")
        }
    }

    test("findDefinition: `test automatic class casting in method parameters - Object`") {
        val text =
            """
              |class CompletionTester {
              | virtual class Base { }
              | virtual class Level1 extends Base { }
              | virtual class Level2 extends Level1 { }
              | private void test() {
              |     Level1 x = new Level2();
              |     meth<CARET>od1(x);
              | }
              |
              | private static void method1(Level2 v) { } // line 11
              | // private static void method1(Level1 v) { } // line 12
              | private static void method1(Object v) { } // line 13
              |}
            """.stripMargin

        val resultNodes = findDefinition(text)
        assert(resultNodes.nonEmpty, "Expected to find non empty result")
        assertResult(1,"Wrong number of results found") (resultNodes.length)
        resultNodes.head match {
            case typeDefinition: IsTypeDefinition =>
                assertResult(Option(QualifiedName(Array("CompletionTester", "method1"))), "Wrong caret type detected. Expected 'method1()'")(typeDefinition.qualifiedName)
                assertResult(13, "expected method on line 13 - Base")(typeDefinition.range.start.line)
            case _ =>
                fail("Failed to locate correct node")
        }
    }

    test("findDefinition: `static method call with different parameters`") {
        val text =
            """
              |class CompletionTester {
              | private void test() {
              |     Id id;
              |     CompletionTester.getOr<CARET>Else(id, 0); // 0 has type Integer
              | }
              |
              | private static Decimal getOrElse(final String val) { } // line 8
              | private static Decimal getOrElse(final String val, final Integer defaultVal) { } // line 9
              |}
            """.stripMargin

        val resultNodes = findDefinition(text, "getOrElse", loadStdLib = true)
        assert(resultNodes.nonEmpty, "Expected to find non empty result")
        assertResult(1,"Wrong number of results found") (resultNodes.length)
        resultNodes.head match {
            case typeDefinition: IsTypeDefinition =>
                assertResult(Option(QualifiedName(Array("CompletionTester", "getOrElse"))), "Wrong caret type detected. Expected 'getOrElse()'")(typeDefinition.qualifiedName)
                assertResult(9, "expected method on line 9")(typeDefinition.range.start.line)
            case _ =>
                fail("Failed to locate correct node")
        }
    }
    test("findDefinition: `array element type`") {
        val text =
            """
              |class CompletionTester {
              | private void test() {
              |     final List<String> strs;
              |     System.debug(strs[0].toLower<CARET>Case());
              | }
              |
              |}
            """.stripMargin

        val resultNodes = findDefinition(text, "test1", loadStdLib = true)
        assert(resultNodes.nonEmpty, "Expected to find non empty result")
        assertResult(1,"Wrong number of results found") (resultNodes.length)
        resultNodes.head match {
            case typeDefinition: IsTypeDefinition =>
                assertResult(Option(QualifiedName(Array("System", "String", "toLowerCase"))), "Wrong caret type detected. Expected 'getOrElse()'")(typeDefinition.qualifiedName)
            case _ =>
                fail("Failed to locate correct node")
        }
    }

    test("findDefinition: `static method call with array element parameter`") {
        val text =
            """
              |class CompletionTester {
              | private void test() {
              |     final List<Id> ids;
              |     CompletionTester.getOr<CARET>Else('', ids[0]); // ids[0] has type Id
              | }
              |
              | private static Decimal getOrElse(final String val, final Id val2) { } // line 8
              | private static Decimal getOrElse(final String val, final Decimal val2) { } // line 9
              |}
            """.stripMargin

        val resultNodes = findDefinition(text, "getOrElse", loadStdLib = true)
        assert(resultNodes.nonEmpty, "Expected to find non empty result")
        assertResult(1,"Wrong number of results found") (resultNodes.length)
        resultNodes.head match {
            case typeDefinition: IsTypeDefinition =>
                assertResult(Option(QualifiedName(Array("CompletionTester", "getOrElse"))), "Wrong caret type detected. Expected 'getOrElse()'")(typeDefinition.qualifiedName)
                assertResult(8, "expected method on line 8")(typeDefinition.range.start.line)
            case _ =>
                fail("Failed to locate correct node")
        }
    }

    test("findDefinition: method by map return value type") {
        val text =
            """
              |class TypeFinderTester {
              |    private static TypeFinderTester cls = new TypeFinderTester();
              |    private M2Type methodWith2Params(Integer i, String s) { // this is wrong method
              |    }
              |
              |    private M2Type methodWith2Params(Decimal s1, String s2) { // this is right method
              |    }
              |
              |    private M2Type methodWith2Params(String s1, String s2) { // this is also wrong method
              |    }
              |
              |    private void definitionTester() {
              |        final Map<Id, Decimal> decimalByIdMap;
              |        final Id myId;
              |        cls.methodWith<CARET>2Params(decimalByIdMap.get(myId), 'str');
              |
              |    }
              |
              |}
            """.stripMargin
        loadDocument(getProject(), new TestDocument(text, "TypeFinderTester"))

        val resultNodes = findDefinition(text, "methodWith2Params", loadStdLib = true)
        assert(resultNodes.nonEmpty, "Expected to find non empty result")
        assertResult(1,"Wrong number of results found") (resultNodes.length)
        resultNodes.head match {
            case typeDefinition: IsTypeDefinition =>
                assertResult(Option(QualifiedName(Array("TypeFinderTester", "methodWith2Params"))), "Wrong caret type detected. Expected 'methodWith2Params()'")(typeDefinition.qualifiedName)
                assertResult(7, "Method from wrong line returned")(typeDefinition.range.start.line)
            case _ =>
                fail("Failed to locate correct node")
        }
    }
    test("findDefinition: collection method return type: Map.get") {
        val text =
            """
              |class TypeFinderTester {
              |    private void definitionTester() {
              |        final Map<Id, Decimal> decimalByIdMap;
              |        final Id myId;
              |        decimalByIdMap.ge<CARET>t(myId);
              |
              |    }
              |
              |}
            """.stripMargin
        loadDocument(getProject(), new TestDocument(text, "TypeFinderTester"))

        val resultNodes = findDefinition(text, "Map.get", loadStdLib = true)
        assert(resultNodes.nonEmpty, "Expected to find non empty result")
        assertResult(1,"Wrong number of results found") (resultNodes.length)
        resultNodes.head match {
            case typeDefinition: IsTypeDefinition =>
                assertResult(Option(QualifiedName(Array("System", "Map", "get"))), "Wrong caret type detected. Expected 'System.Map.get()'")(typeDefinition.qualifiedName)
                assertResult(Option(QualifiedName(Array("Decimal"))), "Wrong caret type detected. Expected 'Decimal'")(typeDefinition.getValueType.map(_.qualifiedName))
            case _ =>
                fail("Failed to locate correct node")
        }
    }
    test("findDefinition: collection method return type: Map.keySet") {
        val text =
            """
              |class TypeFinderTester {
              |    private void definitionTester() {
              |        final Map<Id, Decimal> decimalByIdMap;
              |        final Id myId;
              |        decimalByIdMap.keySe<CARET>t(myId);
              |
              |    }
              |
              |}
            """.stripMargin
        loadDocument(getProject(), new TestDocument(text, "TypeFinderTester"))

        val resultNodes = findDefinition(text, "Map.keySet", loadStdLib = true)
        assert(resultNodes.nonEmpty, "Expected to find non empty result")
        assertResult(1,"Wrong number of results found") (resultNodes.length)
        resultNodes.head match {
            case typeDefinition: IsTypeDefinition =>
                assertResult(Option(QualifiedName(Array("System", "Map", "keySet"))), "Wrong caret type detected.")(typeDefinition.qualifiedName)
                assertResult(Option("Set<Id>"), "Wrong caret type detected.")(typeDefinition.getValueType.map(_.toString))
            case _ =>
                fail("Failed to locate correct node")
        }
    }

    test("findDefinition: collection method return type: List.deepClone") {
        val text =
            """
              |class TypeFinderTester {
              |    private void definitionTester() {
              |        final List<Id> decimalList;
              |        decimalList.deepClon<CARET>e();
              |
              |    }
              |
              |}
            """.stripMargin
        loadDocument(getProject(), new TestDocument(text, "TypeFinderTester"))

        val resultNodes = findDefinition(text, "List.deepClone", loadStdLib = true)
        assert(resultNodes.nonEmpty, "Expected to find non empty result")
        assertResult(1,"Wrong number of results found") (resultNodes.length)
        resultNodes.head match {
            case typeDefinition: IsTypeDefinition =>
                assertResult(Option(QualifiedName(Array("System", "List", "deepClone"))), "Wrong caret type detected.'")(typeDefinition.qualifiedName)
                assertResult(Option("List<Id>"), "Wrong caret type detected.")(typeDefinition.getValueType.map(_.toString))
            case _ =>
                fail("Failed to locate correct node")
        }
    }

    test("findDefinition: in presence of SWITCH statement") {
        val text =
            """
              |  public class TypeFinderTester {
              |    public enum SomeType { ONE, TWO, THREE }
              |
              |    public SomeType myType;
              |
              |    void myMethod (SwitchStatementTester tester) {
              |
              |        myType = SomeType.O<CARET>NE;
              |
              |        switch on tester.myType {
              |            when ONE {
              |            }
              |            when TWO {
              |            }
              |            when else {
              |            }
              |        }
              |
              |    }
              |}
              |            """.stripMargin
        loadDocument(getProject(), new TestDocument(text, "TypeFinderTester"))

        val resultNodes = findDefinition(text, "Switch.tester", loadStdLib = false)
        assert(resultNodes.nonEmpty, "Expected to find non empty result")
        assertResult(1,"Wrong number of results found") (resultNodes.length)
        resultNodes.head match {
            case typeDefinition: IsTypeDefinition =>
                assertResult(Option(QualifiedName(Array("TypeFinderTester", "SomeType", "ONE"))), "Wrong caret type detected.'")(typeDefinition.qualifiedName)
                assertResult(Option(QualifiedName("TypeFinderTester", "SomeType", "ONE")), "Wrong caret type detected.")(typeDefinition.getValueType.map(_.qualifiedName))

            case _ =>
                fail("Failed to locate correct node")
        }
    }
}

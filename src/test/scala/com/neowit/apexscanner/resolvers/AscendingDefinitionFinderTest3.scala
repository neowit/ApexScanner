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
  * tests of AscendingDefinitionFinder in multi-file/class scenarios
  */

class AscendingDefinitionFinderTest3 extends FunSuite {
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

    private def findDefinition(text: String, documentName: String = "test", loadStdLib: Boolean = false): scala.Seq[AstNode] = {
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
    private def loadDocument(project: Project, document: VirtualDocument): Option[AstBuilderResult] = {
        project.getAst(document, forceRebuild = true)
    }


    test("findDefinition: method defined only in parent class") {
        val textParent =
            """
              |class BaseClass {
              | public String method1(Integer i) {
              |     return '';
              | }
              |}
            """.stripMargin
        loadDocument(getProject(), new TestDocument(textParent, "BaseClass"))

        val text =
            """
              |class DefinitionTester extends BaseClass {
              | System.debug(meth<CARET>od1(0));
              |}
            """.stripMargin

        val resultNodes = findDefinition(text)
        assert(resultNodes.nonEmpty, "Expected to find non empty result")
        assertResult(1,"Wrong number of results found") (resultNodes.length)
        resultNodes.head match {
            case typeDefinition: IsTypeDefinition =>
                assertResult(Option(QualifiedName("BaseClass", "method1")), "Wrong caret type detected.")(typeDefinition.qualifiedName)
                assertResult(Option(QualifiedName("String")), "Wrong caret type detected.")(typeDefinition.getValueType.map(_.qualifiedName))
            case _ =>
                fail( "Failed to locate correct node.")
        }
    }

    test("findDefinition: method defined in parent AND child class") {
        val textParent =
            """
              |class BaseClass {
              | public String method1() {
              |     return '';
              | }
              |}
            """.stripMargin
        loadDocument(getProject(), new TestDocument(textParent, "BaseClass"))

        val text =
            """
              |class DefinitionTester extends BaseClass {
              | System.debug(meth<CARET>od1());
              | public String method1() {
              |     return '';
              | }
              |}
            """.stripMargin

        val resultNodes = findDefinition(text)
        assert(resultNodes.nonEmpty, "Expected to find non empty result")
        assertResult(1,"Wrong number of results found") (resultNodes.length)
        resultNodes.head match {
            case typeDefinition: IsTypeDefinition =>
                assertResult(Option(QualifiedName("DefinitionTester", "method1")), "Wrong caret type detected.")(typeDefinition.qualifiedName)
                assertResult(Option(QualifiedName("String")), "Wrong caret type detected.")(typeDefinition.getValueType.map(_.qualifiedName))
            case _ =>
                fail( "Failed to locate correct node.")
        }
    }

    test("findDefinition: method defined in parent AND child class called from SOQL") {
        val textParent =
            """
              |class BaseClass {
              | public String method1(Integer i) {
              |     return '';
              | }
              |}
            """.stripMargin
        loadDocument(getProject(), new TestDocument(textParent, "BaseClass"))

        val text =
            """
              |class DefinitionTester extends BaseClass {
              | System.debug([select Id from Account where Name = : meth<CARET>od1(123)]);
              |}
            """.stripMargin

        val resultNodes = findDefinition(text, loadStdLib = true) // need StdLib to because resolving literal type: Integer
        assert(resultNodes.nonEmpty, "Expected to find non empty result")
        assertResult(1,"Wrong number of results found") (resultNodes.length)
        resultNodes.head match {
            case typeDefinition: IsTypeDefinition =>
                assertResult(Option(QualifiedName("BaseClass", "method1")), "Wrong caret type detected.")(typeDefinition.qualifiedName)
                assertResult(Option(QualifiedName("String")), "Wrong caret type detected.")(typeDefinition.getValueType.map(_.qualifiedName))
            case _ =>
                fail( "Failed to locate correct node.")
        }
    }

    test("findDefinition: method defined in the base of 3 class hierarchy") {
        val textParentLevel1 =
            """
              |class BaseClassLevel1 {
              | public String method1(Integer i) {
              |     return '';
              | }
              |}
            """.stripMargin
        loadDocument(getProject(), new TestDocument(textParentLevel1, "BaseClassLevel1"))

        val textParentLevel2 =
            """
              |class BaseClassLevel2 extends BaseClassLevel1 {
              |}
            """.stripMargin
        loadDocument(getProject(), new TestDocument(textParentLevel2, "BaseClassLevel2"))

        val textClass =
            """
              |class TestClass extends BaseClassLevel2 {
              |}
            """.stripMargin
        loadDocument(getProject(), new TestDocument(textClass, "TestClass"))


        val text =
            """
              |class DefinitionTester {
              | TestClass cls;
              | cls.meth<CARET>od1(123)]);
              |}
            """.stripMargin

        val resultNodes = findDefinition(text, loadStdLib = true) // need StdLib to because resolving literal type: Integer
        assert(resultNodes.nonEmpty, "Expected to find non empty result")
        assertResult(1,"Wrong number of results found") (resultNodes.length)
        resultNodes.head match {
            case typeDefinition: IsTypeDefinition =>
                assertResult(Option(QualifiedName("BaseClassLevel1", "method1")), "Wrong caret type detected.")(typeDefinition.qualifiedName)
                assertResult(Option(QualifiedName("String")), "Wrong caret type detected.")(typeDefinition.getValueType.map(_.qualifiedName))
            case _ =>
                fail( "Failed to locate correct node.")
        }
    }

    test("findDefinition: Method From Inner Class Of Another Class") {
        val textTypeFinder =
            """
              |class TypeFinder {
              |    private M2Type methodWith2Params(Integer i, String s) { // this is wrong method
              |    }
              |
              |    private M2Type methodWith2Params(String s1, String s2) { // this is also wrong method
              |    }
              |
              |    public class InnerClass1 {
              |        public String innerStr1;
              |        public M2Inner_I_S methodWith2Params(Integer i, String s2) { // expect to find this method
              |        }
              |    }
              |
              |}
            """.stripMargin
        loadDocument(getProject(), new TestDocument(textTypeFinder, "TypeFinder"))

        val text =
            """
              |class DefinitionTester {
              |    private void findMethodFromInnerClassOfAnotherClass() {
              |        Integer int = 0;
              |        TypeFinder.InnerClass1 cls1 = new TypeFinder.InnerClass1();
              |        cls1.methodWith<CARET>2Params(int, cls1.innerStr1);
              |    }
              |}
            """.stripMargin

        val resultNodes = findDefinition(text, loadStdLib = true) // need StdLib to because resolving literal type: Integer
        assert(resultNodes.nonEmpty, "Expected to find non empty result")
        assertResult(1,"Wrong number of results found") (resultNodes.length)
        resultNodes.head match {
            case typeDefinition: IsTypeDefinition =>
                assertResult(Option(QualifiedName("TypeFinder", "InnerClass1", "methodWith2Params")), "Wrong caret type detected.")(typeDefinition.qualifiedName)
                assertResult(Option(QualifiedName("M2Inner_I_S")), "Wrong caret type detected.")(typeDefinition.getValueType.map(_.qualifiedName))
            case _ =>
                fail( "Failed to locate correct node.")
        }
    }

    test("findDefinition: Trigger: Method From Inner Class Of Another Class") {
        val textTypeFinder =
            """
              |class TypeFinder {
              |    private M2Type methodWith2Params(Integer i, String s) { // this is wrong method
              |    }
              |
              |    private M2Type methodWith2Params(String s1, String s2) { // this is also wrong method
              |    }
              |
              |    public class InnerClass1 {
              |        public String innerStr1;
              |        public M2Inner_I_S methodWith2Params(Integer i, String s2) { // expect to find this method
              |        }
              |    }
              |
              |}
            """.stripMargin
        loadDocument(getProject(), new TestDocument(textTypeFinder, "TypeFinder"))

        val text =
            """
              |trigger TypeFinderMultiFile on Account (before insert) {
              |    Integer int = 0;
              |    TypeFinder.InnerClass1 cls1 = new TypeFinder.InnerClass1();
              |    cls1.method<CARET>With2Params(int, cls1.innerStr1);
              |}
              |
            """.stripMargin

        val resultNodes = findDefinition(text, loadStdLib = true) // need StdLib to because resolving literal type: Integer
        assert(resultNodes.nonEmpty, "Expected to find non empty result")
        assertResult(1,"Wrong number of results found") (resultNodes.length)
        resultNodes.head match {
            case typeDefinition: IsTypeDefinition =>
                assertResult(Option(QualifiedName("TypeFinder", "InnerClass1", "methodWith2Params")), "Wrong caret type detected.")(typeDefinition.qualifiedName)
                assertResult(Option(QualifiedName("M2Inner_I_S")), "Wrong caret type detected.")(typeDefinition.getValueType.map(_.qualifiedName))
            case _ =>
                fail( "Failed to locate correct node.")
        }
    }
}

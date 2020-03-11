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

import com.neowit.apexscanner.ast.QualifiedName
import com.neowit.apexscanner.symbols.SymbolKind
import com.neowit.apexscanner.{FileBasedDocument, Project, TestConfigProvider}
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatest.funsuite.AnyFunSuite


/**
  * Created by Andrey Gavrikov 
  */
class QualifiedNameDefinitionFinderTest extends AnyFunSuite with TestConfigProvider with ScalaFutures with IntegrationPatience {
    private val projectPath = getTestResourcePath("QualifiedNameDefinitionFinderTest.projectPath")

    test("testFindDefinition: findByDocumentName(TypeFinder.InnerClass1) - using existing AST") {
        val project = Project(projectPath)

        // load AST of test file explicitly
        project.getAst(FileBasedDocument(getTestResourcePath("QualifiedNameDefinitionFinderTest.existingAST.filePath")))
        val finder = new QualifiedNameDefinitionFinder(project)
        val qName = QualifiedName(Array("TypeFiNder","InnerClass1"))
        val res = finder.findDefinition(qName)
        res match {
            case Some(node) =>
                val methods = node.getSymbolsOfKind(SymbolKind.Method)
                assert(methods.exists(_.symbolName == "methodWith2Params"), "TypeFinder.InnerClass1.methodWith2Params not found")
            case None =>
                fail( "Not found: " + qName)
        }

    }

    test("testFindDefinition: findByDocumentName(TypeFinder.InnerClass1) - lazy loading of AST") {
        val project = Project(projectPath)

        val finder = new QualifiedNameDefinitionFinder(project)
        val qName = QualifiedName(Array("TypeFinDer","InnerClASs1"))
        // project has not been loaded yet, see if lazy loading/parsing of Class code works
        // in this test we attempt to find/load AST based on parts of Qualified Name
        val res = finder.findDefinition(qName)
        res match {
            case Some(node) =>
                val methods = node.getSymbolsOfKind(SymbolKind.Method)
                assert(methods.exists(_.symbolName == "methodWith2Params"), "TypeFinder.InnerClass1.methodWith2Params not found")
            case None =>
                fail( "Not found: " + qName)
        }
    }

}

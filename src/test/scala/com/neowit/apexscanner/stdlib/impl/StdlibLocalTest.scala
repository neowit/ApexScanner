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

package com.neowit.apexscanner.stdlib.impl

import java.nio.file.FileSystems

import com.neowit.apexscanner.{Project, TestConfigProvider}
import com.neowit.apexscanner.ast.QualifiedName
import com.neowit.apexscanner.nodes.{MethodNodeBase, MethodNodeType}
import com.neowit.apexscanner.resolvers.QualifiedNameDefinitionFinder
import com.neowit.apexscanner.symbols.SymbolKind
import org.scalatest.FunSuite
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}

/**
  * Created by Andrey Gavrikov 
  */
class StdlibLocalTest extends FunSuite with TestConfigProvider with ScalaFutures with IntegrationPatience {
    private val projectPath = FileSystems.getDefault.getPath("/temp")
    private val project = Project(projectPath)
    project.loadStdLib() // force loading of StandardLibrary

    test("test: System.String") {
        val qName = QualifiedName(Array("System", "String"))
        project.getByQualifiedName(qName) match {
            case Some(node) =>
                val methods = node.getSymbolsOfKind(SymbolKind.Method)
                assert(methods.exists(_.symbolName == "abbreviate"), "String.abbreviate not found")
                assert(methods.exists(_.symbolName.equalsIgnoreCase("addError")), "String.addError not found")
                assert(methods.exists(_.symbolName.equalsIgnoreCase("startsWithIgnoreCase")), "String.startsWithIgnoreCase not found")
                //println(methods)
            case None =>
                fail( "Not found: " + qName)
        }
    }

    test("test: String") {
        val qName = QualifiedName(Array("String"))
        project.getByQualifiedName(qName) match {
            case Some(node) =>
                val methods = node.getSymbolsOfKind(SymbolKind.Method)
                assert(methods.exists(_.symbolName == "abbreviate"), "String.abbreviate not found")
                assert(methods.exists(_.symbolName.equalsIgnoreCase("addError")), "String.addError not found")
                assert(methods.exists(_.symbolName.equalsIgnoreCase("startsWithIgnoreCase")), "String.startsWithIgnoreCase not found")
            //println(methods)
            case None =>
                fail( "Not found: " + qName)
        }
    }

    test("test: SaveResult") {// Database.SaveResult
        val qName = QualifiedName(Array("SaveResult"))
        project.getByQualifiedName(qName) match {
            case Some(node) =>
                val methods = node.getSymbolsOfKind(SymbolKind.Method)
                assert(methods.exists(_.symbolName.equalsIgnoreCase("getErrors")), "SaveResult.getErrors not found")

                node.getChildrenInAst[MethodNodeBase](MethodNodeType).find(_.symbolName.equalsIgnoreCase("getErrors")) match {
                    case Some(m) =>
                        m.getValueType.exists(_.toString.equalsIgnoreCase("List<Database.Error>"))
                    case None =>
                        fail( "SaveResult.getErrors returning 'List<Database.Error>' not found")
                }
                assert(methods.exists(_.symbolName.equalsIgnoreCase("getId")), "SaveResult.getId not found")
                assert(methods.exists(_.symbolName.equalsIgnoreCase("isSuccess")), "SaveResult.isSuccess not found")
            //println(methods)
            case None =>
                fail( "Not found: " + qName)
        }
    }

    test("test: ApexPages.Severity") {
        val finder = new QualifiedNameDefinitionFinder(project)
        val qName = QualifiedName(Array("ApexPages", "Severity"))
        finder.findDefinition(qName) match {
            case Some(node) =>
                val enumConstants = node.getSymbolsOfKind(SymbolKind.Property)
                assert(enumConstants.exists(_.symbolName == "INFO"), "ApexPages.Severity.INFO not found")
                assert(enumConstants.exists(_.symbolName == "ERROR"), "ApexPages.Severity.ERROR not found")
            //println(methods)
            case None =>
                fail( "Not found: " + qName)
        }
    }
}

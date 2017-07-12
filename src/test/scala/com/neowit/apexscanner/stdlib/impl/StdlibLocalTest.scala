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
import com.neowit.apexscanner.symbols.SymbolKind
import org.scalatest.FunSuite
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by Andrey Gavrikov 
  */
class StdlibLocalTest extends FunSuite with TestConfigProvider with ScalaFutures with IntegrationPatience {
    private val projectPath = FileSystems.getDefault.getPath("/temp")
    private val project = Project(projectPath)
    project.getStandardLibrary // force loading of StandardLibrary

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
                assert(false, "Not found: " + qName)
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
                assert(false, "Not found: " + qName)
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
                        assert(false, "SaveResult.getErrors returning 'List<Database.Error>' not found")
                }
                assert(methods.exists(_.symbolName.equalsIgnoreCase("getId")), "SaveResult.getId not found")
                assert(methods.exists(_.symbolName.equalsIgnoreCase("isSuccess")), "SaveResult.isSuccess not found")
            //println(methods)
            case None =>
                assert(false, "Not found: " + qName)
        }
    }
}

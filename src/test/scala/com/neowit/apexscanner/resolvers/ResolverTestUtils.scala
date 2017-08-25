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

import java.nio.file.{FileSystems, Path}

import com.neowit.apexscanner.{FileBasedDocument, Project, TestConfigProvider}
import com.neowit.apexscanner.ast.AstBuilder
import com.neowit.apexscanner.nodes.AstNode
import com.neowit.apexscanner.scanner.ApexcodeScanner
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
/**
  * Created by Andrey Gavrikov 
  */
object ResolverTestUtils extends TestConfigProvider with ScalaFutures with IntegrationPatience{

    /*
    def withPathProperty(pathKey: String)(codeBlock: (String, Path, AstNode, Project) => Any): Unit = {
        // pathKey = "AscendingDefinitionFinderTest.testFindDefinition.path"
        val filePath = getProperty(pathKey)
        val path = FileSystems.getDefault.getPath(filePath)

        val project = Project(path)
        val astBuilder = new AstBuilder(project)
        astBuilder.build(path)

        astBuilder.getAst(path) match {
            case None =>
            // do nothing
            case Some(result) if result.fileScanResult.errors.nonEmpty =>
                println("ERRORS ENCOUNTERED")
                result.fileScanResult.errors.foreach(println(_))

            case Some(result) =>
                val rootNode = result.rootNode
                codeBlock(filePath, path, rootNode, project)
        }
    }
    */

    def getResolverTestData(pathKey: String, existingProject: Option[Project] = None): ResolverTestData = {
        // pathKey = "AscendingDefinitionFinderTest.testFindDefinition.path"
        val filePath = getProperty(pathKey)
        val path = FileSystems.getDefault.getPath(filePath)

        val project = existingProject.getOrElse(Project(path))
        val astBuilder = new AstBuilder(Option(project))
        val document = FileBasedDocument(Option(path))
        Await.result(astBuilder.build(path, ApexcodeScanner.createDefaultScanner(astBuilder)), Duration.Inf)

        astBuilder.getAst(document) match {
            case None =>
            // do nothing
                ResolverTestData(filePath, path, rootNode = None, project)
            case Some(result) if result.fileScanResult.errors.nonEmpty =>
                println("ERRORS ENCOUNTERED")
                result.fileScanResult.errors.foreach(println(_))
                throw new RuntimeException("Syntax errors encountered while parsing: " + pathKey)
            case Some(result) =>
                val rootNode = result.rootNode
                ResolverTestData(filePath, path, Option(rootNode), project)
        }

    }
}
case class ResolverTestData(filePath: String, path: Path, rootNode: Option[AstNode], project: Project)

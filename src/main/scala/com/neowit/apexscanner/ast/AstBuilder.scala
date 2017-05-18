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

package com.neowit.apexscanner.ast

import java.nio.file.{FileSystems, Path}

import com.neowit.apexscanner.Project
import com.neowit.apexscanner.nodes.AstNode
import com.neowit.apexscanner.scanner.actions.SyntaxChecker
import com.neowit.apexscanner.scanner.{FileScanResult, Scanner}

import scala.concurrent.{ExecutionContext, Future}

case class AstBuilderResult(fileScanResult: FileScanResult, rootNode: AstNode)

object AstBuilder {

    def main(args: Array[String]): Unit = {
        import scala.concurrent.ExecutionContext.Implicits.global

        val path = FileSystems.getDefault.getPath(args(0))
        val builder = new AstBuilder(Project(path))
        builder.build(path)
        ()
    }
}

class AstBuilder(project: Project) {
    val DEFAULT_SCANNER = new Scanner(Scanner.defaultIsIgnoredPath, onEachFileScanResult, SyntaxChecker.errorListenerCreator)

    private val astCache = Map.newBuilder[Path, AstBuilderResult]
    private val fileNameCache = Map.newBuilder[String, Path]

    def build(path: Path, scanner: Scanner = DEFAULT_SCANNER)(implicit ex: ExecutionContext): Future[Unit] = {
        scanner.scan(path)
    }

    private def onEachFileScanResult(result: FileScanResult): Unit = {
        val visitor = new ASTBuilderVisitor(project, result.sourceFile)
        val compileUnit = visitor.visit(result.parseContext)
        //new AstWalker().walk(compileUnit, new DebugVisitor)
        val sourceFile = result.sourceFile
        astCache += sourceFile -> AstBuilderResult(result, compileUnit)
        fileNameCache += sourceFile.getFileName.toString -> result.sourceFile
        // record all ClassLike nodes in project
        visitor.getClassLikeNodes.foreach(project.addByQualifiedName(_))
    }
    private def getPath(fileName: String): Option[Path] = {
        fileNameCache.result().get(fileName)
    }

    /**
      * get cached AST for given file
      * @param path single file path
      * @return
      */
    def getAst(path: Path): Option[AstBuilderResult] = {
        astCache.result().get(path)
    }
    def getAstByFilename(fileName: String): Option[AstBuilderResult] = {
        getPath(fileName).flatMap(path => getAst(path))
    }
}


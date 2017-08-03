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

import com.neowit.apexscanner.{FileBasedDocument, Project, VirtualDocument}
import com.neowit.apexscanner.nodes.{AstNode, EnumNode}
import com.neowit.apexscanner.scanner.actions.SyntaxChecker
import com.neowit.apexscanner.scanner.{FileScanResult, Scanner}
import org.antlr.v4.runtime.atn.PredictionMode

import scala.concurrent.{ExecutionContext, Future}

case class AstBuilderResult(fileScanResult: FileScanResult, rootNode: AstNode)

object AstBuilder {

    def main(args: Array[String]): Unit = {
        import scala.concurrent.ExecutionContext.Implicits.global

        val path = FileSystems.getDefault.getPath(args(0))
        val document = FileBasedDocument(path)
        val builder = new AstBuilder(Project(path))
        builder.build(document)
        ()
    }
}

class AstBuilder(project: Project) {
    val DEFAULT_SCANNER = new Scanner(Scanner.defaultIsIgnoredPath, onEachFileScanResult, SyntaxChecker.errorListenerCreator)

    private val astCache = new collection.mutable.HashMap[VirtualDocument.DocumentId, AstBuilderResult]
    private val fileNameCache = Map.newBuilder[String, VirtualDocument]

    def build(path: Path, scanner: Scanner)(implicit ex: ExecutionContext): Future[Unit] = {
        scanner.scan(path)
    }
    def build(document: VirtualDocument, scanner: Scanner = DEFAULT_SCANNER, predictionMode: PredictionMode = PredictionMode.SLL): Future[Unit] = {
        val scanResult = scanner.scan(document, predictionMode)
        onEachFileScanResult(scanResult)
        Future.successful(())
    }

    private def onEachFileScanResult(result: FileScanResult): Unit = {
        val visitor = new ASTBuilderVisitor(Option(project), Option(result.document))
        val compileUnit = visitor.visit(result.parseContext)
        //new AstWalker().walk(compileUnit, new DebugVisitor)
        val sourceDocument = result.document
        astCache += sourceDocument.getId -> AstBuilderResult(result, compileUnit)
        fileNameCache += sourceDocument.getFileName.toString -> sourceDocument
        // record all ClassLike nodes in project
        visitor.getClassLikeNodes.foreach(project.addByQualifiedName(_))

        // add standard ENUM method - have to do it here because need to make sure that full parent/child hierarchy is already in place
        visitor.getClassLikeNodes.filter(_.isInstanceOf[EnumNode])
            .foreach{
                case enumNode: EnumNode => EnumNode.addStandardMethods(enumNode)
            }

    }
    private def getDocument(fileName: String): Option[VirtualDocument] = {
        fileNameCache.result().get(fileName)
    }

    /**
      * get cached AST for given file
      * @param document single document
      * @return
      */
    def getAst(document: VirtualDocument): Option[AstBuilderResult] = {
        astCache.get(document.getId)
    }
    def getAstByFilename(fileName: String): Option[AstBuilderResult] = {
        getDocument(fileName).flatMap(path => getAst(path))
    }
}


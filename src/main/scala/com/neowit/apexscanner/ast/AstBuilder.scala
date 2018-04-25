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

import java.nio.file.Path

import com.neowit.apexscanner.{Project, VirtualDocument}
import com.neowit.apexscanner.nodes.AstNode
import com.neowit.apexscanner.scanner.{DocumentScanResult, Scanner}
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.atn.PredictionMode

import scala.concurrent.{ExecutionContext, Future}

case class AstBuilderResult(fileScanResult: DocumentScanResult, rootNode: AstNode)

object AstBuilder {
    type VisitorCreatorFun = (Option[Project], Option[VirtualDocument], Option[CommonTokenStream]) => AstBuilderVisitor
}

class AstBuilder(projectOpt: Option[Project], visitorCreator: AstBuilder.VisitorCreatorFun = ApexAstBuilderVisitor.VISITOR_CREATOR_FUN) {self =>

    private val _astCache = new collection.mutable.HashMap[VirtualDocument.DocumentId, AstBuilderResult]
    private val _documentByFileNameCache = new collection.mutable.HashMap[String, VirtualDocument]

    def build(path: Path, scanner: Scanner)(implicit ex: ExecutionContext): Future[Unit] = {
        scanner.scan(path)
    }

    def build(document: VirtualDocument, scanner: Scanner, predictionMode: PredictionMode = PredictionMode.SLL): Unit = {
        val scanResult = scanner.scan(document, predictionMode, None)
        onEachFileScanResult(scanResult)
        ()
    }

    def onEachFileScanResult(result: DocumentScanResult): DocumentScanResult = {
        //val visitor = new ApexAstBuilderVisitor(Option(project), Option(result.document))
        val visitor = visitorCreator(projectOpt, Option(result.document), Option(result.tokenStream))
        val compileUnit = visitor.visit(result.parseContext)
        //new AstWalker().walk(compileUnit, new DebugVisitor)
        val sourceDocument = result.document
        _astCache += sourceDocument.getId -> AstBuilderResult(result, compileUnit)
        sourceDocument.getFileName.foreach{filePath =>
            _documentByFileNameCache += getFileName(filePath) -> sourceDocument
        }
        // finalise
        visitor.onComplete()
        result
    }

    private def getFileName(path: Path): String = {
        path.getFileName.toString.toLowerCase
    }
    private def getDocument(fileName: String): Option[VirtualDocument] = {
        _documentByFileNameCache.result().get(fileName.toLowerCase)
    }

    /**
      * get cached AST for given file
      * @param document single document
      * @return
      */
    def getAst(document: VirtualDocument): Option[AstBuilderResult] = {
        _astCache.get(document.getId)
    }

    /**
      * @param fileName last part of file path, e.g. /some/path/Name.cls => Name.cls
      * @return
      */
    def getAstByFilename(fileName: String): Option[AstBuilderResult] = {
        getDocument(fileName).flatMap(doc => getAst(doc))
    }
    def getAstByFilePath(path: Path): Option[AstBuilderResult] = {
        getDocument(getFileName(path)).flatMap(doc => getAst(doc))
    }

    /**
      * remove existing AST build based on specified document
      */
    def clearDocumentAst(path: Path): Option[VirtualDocument] = {
        val fileName = getFileName(path)
        getDocument(fileName).map(doc => _astCache.remove(doc.getId) )
        _documentByFileNameCache.remove(fileName)
    }
}


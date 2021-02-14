/*
 * Copyright (c) 2017 Andrey Gavrikov.
 *
 * This file is part of https://github.com/neowit/apexscanner
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.neowit.apexscanner.scanner

import java.nio.file.attribute.BasicFileAttributes
import java.nio.file.{FileVisitResult, Files, Path, SimpleFileVisitor}

import com.neowit.apexscanner.{FileBasedDocument, VirtualDocument}
import com.neowit.apexscanner.scanner.actions.SyntaxError
import org.antlr.v4.runtime._
import org.antlr.v4.runtime.atn.PredictionMode

import scala.concurrent.{ExecutionContext, Future}

case class DocumentScanResult(document: VirtualDocument, errors: Seq[SyntaxError], parseContext: ParserRuleContext, tokenStream: CommonTokenStream)

object Scanner{

    /**
      * dummy method which can be used in Scanner.onEachResult parameter when nothing needs to be done on each result
      */
    def defaultOnEachResult(result: DocumentScanResult): DocumentScanResult = result

    private val ignoredDirs = Set("resources_unpacked", "Referenced Packages")
    def defaultIsIgnoredPath(path: Path): Boolean = {
        val isDirectory = Files.isDirectory(path)
        val fileName = path.getName(path.getNameCount-1).toString
        if (Files.isReadable(path)) {
            if (isDirectory) {
                return ignoredDirs.contains(fileName)
            } else {
                //regular file
                if (
                    (fileName.endsWith(".cls") || fileName.endsWith(".trigger"))
                      && !fileName.contains("__") // exclude classes with namespace <Namespace>__classname.cls, they do not have apex code
                ) {
                    return false // not ignored file
                }
            }
            true
        } else {
            true //unreadable files are ignored
        }
    }
}

/**
  * Parse & Check syntax in files residing in specified path/location
  */
abstract class Scanner() {
    /**
      * checks if path points to a folder and certain paths inside need to be ignored
      * @return
      */
    def isIgnoredPath(path: Path): Boolean

    /**
      * implement this function if additional action is required when result for each individual file is available
      * @return
      */
    def onEachResult(result: DocumentScanResult): DocumentScanResult

    /**
      * factory method creating syntax error listener
      * @return
      */
    def errorListenerFactory(document: VirtualDocument): ApexErrorListener

    /**
      * implement this method with logic needed to scan single document
      * @param document VirtualDocument to scan
      * @param predictionMode necessary prediction mode
      * @param documentTokenStreamOpt this option is provided if document token stream is already available
      * @return
      */
    def scan(document: VirtualDocument, predictionMode: PredictionMode, documentTokenStreamOpt: Option[CommonTokenStream]): DocumentScanResult

    /**
      * Parse & Check syntax in files residing in specified path/location
      * @param path file or folder with eligible apex files to check syntax
      * @return
      */
    def scan(path: Path, predictionMode: PredictionMode = PredictionMode.SLL)(implicit ex: ExecutionContext): Future[Unit] = Future {
        val fileListBuilder = List.newBuilder[Path]

        val apexFileVisitor = new SimpleFileVisitor[Path]() {
            override def visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult = {
                if (!attrs.isDirectory && !isIgnoredPath(file) ) {
                    fileListBuilder += file
                }
                FileVisitResult.CONTINUE
            }

            override def preVisitDirectory(dir: Path, attrs: BasicFileAttributes): FileVisitResult = {
                if (isIgnoredPath(dir)) {
                    FileVisitResult.SKIP_SUBTREE
                } else {
                    super.preVisitDirectory(dir, attrs)
                }
            }
        }

        Files.walkFileTree( path, apexFileVisitor)
        val files = fileListBuilder.result()

        files.foreach{ file =>
            val document = FileBasedDocument(file)
            val scanResult = scan(document, predictionMode, documentTokenStreamOpt = None)
            onEachResult(scanResult)
        }
    }


}



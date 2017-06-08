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

package com.neowit.apexscanner.scanner.actions

import java.nio.file.Path

import com.neowit.apexscanner.scanner._
import org.antlr.v4.runtime.{BaseErrorListener, RecognitionException, Recognizer}

import scala.concurrent.{ExecutionContext, Future}

object SyntaxChecker {
    def errorListenerCreator(file: Path): ApexErrorListener = {
        new SyntaxCheckerErrorListener(file)
    }
}
class SyntaxChecker {
    import SyntaxChecker._
    /**
      * Parse & Check syntax in files residing in specified path/location
      * @param path file or folder with eligible apex files to check syntax
      * @param isIgnoredPath - provide this function if path points to a folder and certain paths inside need to be ignored
      * @param onEachResult - provide this function if additional action is required when result for each individual file is available
      * @return sequence of syntax check results
      */
    def check(path: Path,
             isIgnoredPath: Path => Boolean,
             onEachResult: FileScanResult => Unit)(implicit ex: ExecutionContext): Future[Seq[SyntaxCheckResult]] = {

        val resultBuilder = Seq.newBuilder[SyntaxCheckResult]

        def onFileCheckResult(result: FileScanResult):Unit = {
            val sourceFile = result.sourceFile
            //val fileName = result.sourceFile.getName(sourceFile.getNameCount-1).toString
            val res = SyntaxCheckResult(sourceFile, result.errors)
            resultBuilder += res
            onEachResult(result)
        }
        val scanner = new Scanner(isIgnoredPath, onFileCheckResult, errorListenerCreator)
        scanner.scan(path).map(ignored => resultBuilder.result())
    }
}

case class SyntaxError(offendingSymbol: scala.Any,
                       line: Int,
                       charPositionInLine: Int,
                       msg: String)

private class SyntaxCheckerErrorListener(file: Path) extends BaseErrorListener with ApexErrorListener {
    private val errorBuilder = Seq.newBuilder[SyntaxError]
    override def syntaxError(recognizer: Recognizer[_, _],
                             offendingSymbol: scala.Any,
                             line: Int, charPositionInLine: Int,
                             msg: String,
                             e: RecognitionException): Unit = {
        //super.syntaxError(recognizer, offendingSymbol, line, charPositionInLine, msg, e)
        val error = SyntaxError(offendingSymbol, line, charPositionInLine, msg)
        errorBuilder += error
        //assert(false, "\n" + file.toString + s"\n=> ($line, $charPositionInLine): " + msg)
    }
    def result(): Seq[SyntaxError] = errorBuilder.result()

    /**
      * clear accumulated errors
      * @return
      */
    def clear(): Unit = {
        errorBuilder.clear()
    }
}
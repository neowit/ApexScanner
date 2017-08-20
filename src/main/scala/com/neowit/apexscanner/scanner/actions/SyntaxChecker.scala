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

import com.neowit.apexscanner.antlr.ApexcodeLexer
import com.neowit.apexscanner.{TokenBasedDocument, VirtualDocument}
import com.neowit.apexscanner.nodes.{Language, Position}
import com.neowit.apexscanner.scanner._
import org.antlr.v4.runtime.atn.PredictionMode
import org.antlr.v4.runtime._

import scala.concurrent.{ExecutionContext, Future}
import collection.JavaConverters._

object SyntaxChecker {
    def errorListenerCreator(document: VirtualDocument): ApexErrorListener = {
        new SyntaxCheckerErrorListener(document)
    }

    private def checkSoqlStatements(soqlScanner: SoqlScanner, scanResult: DocumentScanResult): Int = {
        var count = 0
        val file = scanResult.document.getFileName
        getSoqlStatements(scanResult.tokenStream).foreach{soqlToken =>
            checkSoqlStatement(soqlScanner, file, soqlToken)
            count += 1
        }
        count
    }

    private def checkSoqlStatement(soqlScanner: SoqlScanner, file: Path, soqlToken: Token): Unit = {
        println("SOQL: " + soqlToken.getText)
        val scanResult = soqlScanner.scan(TokenBasedDocument(soqlToken, file), PredictionMode.SLL)

        soqlScanner.onEachResult(scanResult)
    }

    def getSoqlStatements(tokenStream: CommonTokenStream): List[Token] = {
        val listBuilder = List.newBuilder[Token]
        for ( token <- tokenStream.getTokens.asScala ) {
            if (Token.DEFAULT_CHANNEL == token.getChannel && ApexcodeLexer.SoqlLiteral == token.getType) {
                listBuilder += token
            }
        }
        listBuilder.result()
    }
}

class SyntaxChecker(secondaryLanguages: Set[Language] = Set(Language.ApexCode)) {
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
             onEachResult: DocumentScanResult => Unit)(implicit ex: ExecutionContext): Future[Seq[SyntaxCheckResult]] = {

        val resultBuilder = Seq.newBuilder[SyntaxCheckResult]

        def onSoqlStatementCheckResult(scanResult: DocumentScanResult):Unit = {
            scanResult.document match {
                case _doc @ TokenBasedDocument(token, _) =>
                    val res = SyntaxCheckResult(_doc, scanResult.errors, language = Language.SOQL)
                    resultBuilder += res
                    onEachResult(scanResult)
                case _ => // do not expect any other document types here, do nothing
            }
        }

        val soqlScanner = new SoqlScanner(
            p => true,
            onEachResult = onSoqlStatementCheckResult,
            errorListenerFactory = errorListenerCreator
        )

        def onApexFileCheckResult(result: DocumentScanResult):Unit = {
            val res = SyntaxCheckResult(result.document, result.errors, language = Language.ApexCode)
            resultBuilder += res
            onEachResult(result)
            if (secondaryLanguages.contains(Language.SOQL)) {
                checkSoqlStatements(soqlScanner, result)
            }
            ()
        }

        val apexScanner = new ApexcodeScanner(isIgnoredPath, onApexFileCheckResult, errorListenerCreator)
        apexScanner.scan(path).map(ignored => resultBuilder.result())

    }

}

/**
  *
  * @param offset if document is inside another document (e.g. SOQL statement inside Apex class)
  *               then error position in the outer document needs to be adjusted
  */
case class SyntaxError(offendingSymbol: scala.Any,
                       line: Int,
                       charPositionInLine: Int,
                       msg: String,
                       offset: Option[Position] = None
                      ) {

    def getAdjustedLine: Int = {
        offset match {
            case Some(Position(offsetLine, _)) =>
                offsetLine + line - 1
            case None => line
        }
    }

    def getAdjustedCharPositionInLine: Int = {
        offset match {
            case Some(Position(_, offsetCol)) =>
                if (1 == line) {
                    offsetCol + charPositionInLine
                } else {
                    charPositionInLine
                }
            case None => charPositionInLine
        }
    }
}

private class SyntaxCheckerErrorListener(document: VirtualDocument) extends BaseErrorListener with ApexErrorListener {
    private val errorBuilder = Seq.newBuilder[SyntaxError]

    override def syntaxError(recognizer: Recognizer[_, _],
                             offendingSymbol: scala.Any,
                             line: Int, charPositionInLine: Int,
                             msg: String,
                             e: RecognitionException): Unit = {
        //super.syntaxError(recognizer, offendingSymbol, line, charPositionInLine, msg, e)
        // make sure msg does not include 'FIXER_TOKEN', end user has no use for it
        val msgCleaned = msg.replace("'FIXER_TOKEN', ", "")
        val error =
        document match {
            case _doc @ TokenBasedDocument(token, _) => // adjust error location
                val offset = Position(token.getLine, token.getCharPositionInLine)
                SyntaxError(offendingSymbol, line, charPositionInLine, msgCleaned, Option(offset))
            case _ => // return as is
                SyntaxError(offendingSymbol, line, charPositionInLine, msgCleaned)
        }
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
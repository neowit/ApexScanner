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

package com.neowit.apexscanner.scanner

import java.nio.file.Path

import com.neowit.apexscanner.{TokenBasedDocument, VirtualDocument}
import com.neowit.apexscanner.antlr.{ApexcodeLexer, SoqlLexer, SoqlParser}
import org.antlr.v4.runtime._
import org.antlr.v4.runtime.atn.PredictionMode
import org.antlr.v4.runtime.misc.ParseCancellationException

import scala.util.{Failure, Success, Try}
import collection.JavaConverters._

/**
  * Created by Andrey Gavrikov 
  */
object SoqlScanner {
    def getSoqlStatements(tokenStream: CommonTokenStream): List[Token] = {
        val listBuilder = List.newBuilder[Token]
        for ( token <- tokenStream.getTokens.asScala ) {
            if (Token.DEFAULT_CHANNEL == token.getChannel && ApexcodeLexer.SoqlLiteral == token.getType) {
                listBuilder += token
            }
        }
        listBuilder.result()
    }
    def defaultIsIgnoredPath(path: Path): Boolean = {
        true
    }
}

abstract class SoqlScanner() extends Scanner() {

    override def isIgnoredPath(path: Path): Boolean

    override def onEachResult(result: DocumentScanResult):DocumentScanResult

    override def errorListenerFactory(document: VirtualDocument): ApexErrorListener
    /**
      *
      * @param document VirtualDocument to scan
      * @param predictionMode necessary prediction mode
      * @param documentTokenStreamOpt if this option is provided then document token stream is already available
      *                               e.g. if ApexClass was previously scanned by apex scanner then
      *                               documentTokenStreamOpt will contain tokenStream of apex class
      *                               and SOQL statements may be found inside this tokenStream
      * @return
      */
    override def scan(document: VirtualDocument, predictionMode: PredictionMode, documentTokenStreamOpt: Option[CommonTokenStream]): DocumentScanResult = {
        documentTokenStreamOpt match {
            case Some(tokenStream) =>
                val file = document.getFileName
                var totalScanResult = DocumentScanResult(document, errors = Seq.empty, null, tokenStream)

                SoqlScanner.getSoqlStatements(tokenStream).foreach{soqlToken =>
                    //println("SOQL: " + soqlToken.getText)
                    val scanResult = scan(TokenBasedDocument(soqlToken, file), PredictionMode.SLL)
                    totalScanResult = scanResult.copy(errors = totalScanResult.errors ++ scanResult.errors)
                    onEachResult(totalScanResult)
                }

                totalScanResult
            case None =>
                scan(document, predictionMode)
        }

    }

    private def scan(document: VirtualDocument, predictionMode: PredictionMode): DocumentScanResult = {
        val lexer = new SoqlLexer(document.getCharStream)

        val tokenStream = new CommonTokenStream(lexer)
        val parser = new SoqlParser(tokenStream)
        // do not dump parse errors into console (or any other default listeners)
        parser.removeErrorListeners()
        val errorListener = errorListenerFactory(document)
        parser.addErrorListener(errorListener)
        parser.setErrorHandler(new BailErrorStrategy)
        parser.getInterpreter.setPredictionMode(predictionMode)

        // run actual scan
        val compilationUnit:ParserRuleContext =
            Try(parser.compilationUnit()) match {
                case Success(tree) => tree
                case Failure(e:ParseCancellationException) if PredictionMode.LL != predictionMode =>
                    // repeat scan with PredictionMode.LL
                    tokenStream.seek(0)
                    errorListener.clear()
                    parser.reset()
                    parser.setErrorHandler(new DefaultErrorStrategy)
                    parser.getInterpreter.setPredictionMode(PredictionMode.LL)
                    val tree = parser.compilationUnit()
                    tree
                case Failure(e) => throw e
            }

        val errors = errorListener.result()
        DocumentScanResult(document, errors, compilationUnit, tokenStream)
    }
}

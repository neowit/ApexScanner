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

import com.neowit.apexscanner.VirtualDocument
import com.neowit.apexscanner.antlr.{ApexcodeLexer, ApexcodeParser}
import org.antlr.v4.runtime.{BailErrorStrategy, CommonTokenStream, DefaultErrorStrategy, ParserRuleContext}
import org.antlr.v4.runtime.atn.PredictionMode
import org.antlr.v4.runtime.misc.ParseCancellationException

import scala.util.{Failure, Success, Try}

/**
  * Created by Andrey Gavrikov 
  */
class ApexcodeScanner(isIgnoredPath: Path => Boolean = Scanner.defaultIsIgnoredPath,
                      onEachResult: FileScanResult => Unit = Scanner.emptyOnEachResult,
                      errorListenerFactory: VirtualDocument => ApexErrorListener) extends Scanner(isIgnoredPath, onEachResult, errorListenerFactory) {

    def scan(document: VirtualDocument, predictionMode: PredictionMode): FileScanResult = {
        val lexer = new ApexcodeLexer(document.getCharStream)

        val tokenStream = new CommonTokenStream(lexer)
        val parser = new ApexcodeParser(tokenStream)
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
        FileScanResult(document, errors, compilationUnit, tokenStream)
    }

}
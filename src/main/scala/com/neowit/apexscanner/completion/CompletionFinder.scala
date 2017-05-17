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

package com.neowit.apexscanner.completion

import java.nio.file.Path

import com.neowit.apexscanner.Project
import com.neowit.apexscanner.antlr.{ApexParserUtils, ApexcodeParser}
import com.typesafe.scalalogging.LazyLogging
import org.antlr.v4.runtime.CommonTokenStream

/**
  * Created by Andrey Gavrikov 
  */
class CompletionFinder(project: Project) extends LazyLogging {

    private def findCaretToken(file: Path, line: Int, column: Int): Option[CaretReachedException] = {
        val caret = new CaretInFile(line, column, file)
        val lexer = ApexParserUtils.getDefaultLexer(file)
        val tokenSource = new CodeCompletionTokenSource(lexer, caret)
        val tokens: CommonTokenStream = new CommonTokenStream(tokenSource)
        val parser = new ApexcodeParser(tokens)
        // do not dump parse errors into console
        ApexParserUtils.removeConsoleErrorListener(parser)
        parser.setBuildParseTree(true)
        parser.setErrorHandler(new CompletionErrorStrategy())
        try {
            // run actual scan, trying to identify caret position
            parser.compilationUnit()
            None
        } catch {
            case ex: CaretReachedException =>
                //println("found caret?")
                logger.debug(ex.caretToken.getText)
                Option(ex)
            //return (CompletionUtils.breakExpressionToATokens(ex), Some(ex))
            case e:Throwable =>
                logger.debug(e.getMessage)
                None
        }
    }

    def find(file: Path, line: Int, column: Int): Unit = {
        findCaretToken(file, line, column) match {
          case Some(caretReachedException) =>

              //caretReachedException.finalContext
              //TODO - now when we found token corresponding caret position try to understand context

          case None => ()
        }
    }
}

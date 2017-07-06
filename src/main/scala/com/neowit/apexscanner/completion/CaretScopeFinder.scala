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

import com.neowit.apexscanner.Project
import com.neowit.apexscanner.antlr.ApexcodeParser
import com.typesafe.scalalogging.LazyLogging
import org.antlr.v4.runtime.Token
import org.antlr.v4.runtime.atn.PredictionMode

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by Andrey Gavrikov
  * given caret position in Document - try to find its Scope/Type
  */
object CaretScopeFinder extends LazyLogging {
    def findCaretToken(caret: CaretInDocument, parser: ApexcodeParser): Option[Token] = {
        //val lexer = ApexParserUtils.getDefaultLexer(caret.document)
        //val tokens = new CommonTokenStream(lexer)
        //val parser = new ApexcodeParser(tokens)
        //// do not dump parse errors into console (or any other default listeners)
        //parser.removeErrorListeners()
        //parser.setErrorHandler(new BailErrorStrategy)
        ////parser.getInterpreter.setPredictionMode(PredictionMode.SLL)
        parser.getInterpreter.setPredictionMode(PredictionMode.LL)
        try {
            // run actual scan, trying to identify caret position
            parser.compilationUnit()
            //val tree = parser.compilationUnit()
            //print(tree.toStringTree(parser))
        } catch {
            case e:Throwable =>
                logger.debug(e.getMessage)
        }
        var i = 0
        val tokens = parser.getInputStream
        var token: Token = tokens.get(i)
        while (caret.isAfter(token) && Token.EOF != token.getType) {
            i += 1
            token = tokens.get(i)
        }
        if (caret.isInside(token) || caret.isBefore(token)) {
            Option(token)
        } else {
            None
        }
    }
}

class CaretScopeFinder(project: Project)(implicit ex: ExecutionContext) extends LazyLogging {
    import CaretScopeFinder._

    def findCaretScope(caret: CaretInDocument, parser: ApexcodeParser): Future[Option[FindCaretScopeResult]] = {
        findCaretToken(caret, parser) match {
            case Some(caretToken) =>
                //now when we found token corresponding caret position try to understand context
                //collectCandidates(caret, caretToken, parser)
                val resolver = new CaretExpressionResolver(project)
                val tokens = parser.getTokenStream
                resolver.resolveCaretScope(caret, caretToken, tokens).map{
                    case caretScopeOpt @ Some( CaretScope(_, _)) =>
                        Option(FindCaretScopeResult(caretScopeOpt, caretToken))
                    case _  =>
                        Option(FindCaretScopeResult(None, caretToken))
                }
            case None =>
                Future.successful(None)
        }
    }

}

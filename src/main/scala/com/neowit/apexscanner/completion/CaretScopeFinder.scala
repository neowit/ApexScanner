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

import com.neowit.apexscanner.{Project, TextBasedDocument, VirtualDocument}
import com.neowit.apexscanner.antlr.{ApexParserUtils, ApexcodeLexer}
import com.typesafe.scalalogging.LazyLogging
import org.antlr.v4.runtime.{CommonTokenStream, Token, TokenStreamRewriter}

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by Andrey Gavrikov
  * given caret position in Document - try to find its Scope/Type
  */
object CaretScopeFinder extends LazyLogging {

    def findCaretToken(caret: CaretInDocument, tokenStream: CommonTokenStream): Option[Token] = {
        tokenStream.fill()
        var i = 0
        val tokens = tokenStream
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

    /**
      * insert or replace token in caret position with FIXER_TOKEN
      * @return updated document (if caret found)
      */
    def injectFixerToken(caret: CaretInDocument): VirtualDocument = {
        val lexer = ApexParserUtils.getDefaultLexer(caret.document)
        val tokens = new CommonTokenStream(lexer)
        val rewriter = new TokenStreamRewriter(tokens)
        val fixerTokenText = lexer.getVocabulary.getSymbolicName(ApexcodeLexer.FIXER_TOKEN)

        CaretScopeFinder.findCaretToken(caret, tokens) match {
            case Some(caretToken) if caretToken.getText.isEmpty || !ApexParserUtils.isWordToken(caretToken)=>
                rewriter.insertBefore(caretToken, fixerTokenText)
            case Some(caretToken) if caretToken.getText.nonEmpty && ApexParserUtils.isWordToken(caretToken) =>
                rewriter.replace(caretToken.getTokenIndex, fixerTokenText)
            case _ => // TODO
        }
        val fixedDocument = TextBasedDocument(rewriter.getText, caret.document.getFileName)
        fixedDocument
    }
}

class CaretScopeFinder(project: Project)(implicit ex: ExecutionContext) extends LazyLogging {
    import CaretScopeFinder._

    def findCaretScope(caretInOriginalDocument: CaretInDocument): Future[Option[FindCaretScopeResult]] = {
        // alter original document by injecting FIXER_TOKEN
        val fixedDocument = injectFixerToken(caretInOriginalDocument)
        val caret = new CaretInFixedDocument(caretInOriginalDocument.position, fixedDocument, caretInOriginalDocument.document)
        val (parser, tokenStream) = ApexParserUtils.createParserWithCommonTokenStream(caret)

        findCaretToken(caret, tokenStream) match {
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

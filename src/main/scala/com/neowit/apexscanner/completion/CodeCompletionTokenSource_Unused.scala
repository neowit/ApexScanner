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

import com.neowit.apexscanner.antlr.ApexParserUtils
import org.antlr.v4.runtime._

/**
  * Created by Andrey Gavrikov 
  */
class CodeCompletionTokenSource_Unused(source: TokenSource, caret: Caret) extends TokenSource {

    private var tokenFactory: TokenFactory[_] = CommonTokenFactory.DEFAULT
    private var caretToken: Token = null
    private val tokenFactorySourcePair = new misc.Pair(source, source.getInputStream)

    private var prevToken: Token = null

    def getLine: Int = {
        source.getLine
    }

    def getCharPositionInLine: Int = {
        source.getCharPositionInLine
    }

    def getInputStream: CharStream = {
        source.getInputStream
    }

    def getSourceName: String = {
        source.getSourceName
    }
    def nextToken: Token = {
        if (null == caretToken) {
            var token: Token = source.nextToken
            //println("caretAToken=" + token.toString)
            if (caret.isAfter(token)) {
                //before target caretAToken
                prevToken = token

            } else if (caret.isBefore(token)) {
                //caretAToken = CaretToken(tokenFactorySourcePair, Token.DEFAULT_CHANNEL, caretOffset, caretOffset)
                //found target?
                //caretToken = prevToken
                //TODO calculate real caret offset
                val startOffset = token.getStartIndex
                val stopOffset = startOffset
                //val t = CaretToken(tokenFactorySourcePair, Token.DEFAULT_CHANNEL, caretOffset, caretOffset)
                val t = CaretToken(tokenFactorySourcePair, Token.DEFAULT_CHANNEL, startOffset, stopOffset)
                t.setCaret(caret)
                t.setOriginalToken(token)
                t.prevToken = prevToken
                token = t
                caretToken = token
            } else { //caret.isInside(token)
                /*
                if (token.getStopIndex + 1 == caretOffset && token.getStopIndex >= token.getStartIndex) {
                    if (!ApexParserUtils.isWordToken(token)) {
                        return token
                    }
                }
                */
                /*
                if (token.getLine == caret.line && token.getCharPositionInLine + 1 == caret.col) {
                    return token
                }
                */
                if (token.getLine == caret.line && token.getCharPositionInLine + 1 == caret.col && token.getStopIndex >= token.getStartIndex) {
                    if (!ApexParserUtils.isWordToken(token)) {
                        return token
                    }
                }
                val t = CaretToken(token)
                t.setCaret(caret)
                t.prevToken = prevToken
                token = t
                caretToken = token
            }
            return token
        }

        throw new UnsupportedOperationException("Attempted to look past the caret.")
    }

    def getTokenFactory: TokenFactory[_] = {
        tokenFactory
    }

    def setTokenFactory(tokenFactory: TokenFactory[_]): Unit = {
        source.setTokenFactory(tokenFactory)
        this.tokenFactory = if (tokenFactory != null) tokenFactory else CommonTokenFactory.DEFAULT
    }
}

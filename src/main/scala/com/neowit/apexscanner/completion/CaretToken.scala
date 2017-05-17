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

import com.typesafe.scalalogging.LazyLogging
import org.antlr.v4.runtime.{CharStream, Token, TokenSource, misc}

/**
  * Created by Andrey Gavrikov 
  */
object CaretToken {
    final val CARET_TOKEN_TYPE: Int = -2
    def apply(tokenFactorySourcePair: misc.Pair[TokenSource, CharStream], channel: Int, start: Int, stop: Int): CaretToken = {
        new org.antlr.v4.runtime.CommonToken(tokenFactorySourcePair, CaretToken.CARET_TOKEN_TYPE, channel, start, stop) with CaretToken
    }

    def apply(oldToken: Token): CaretToken = {
        val token = new org.antlr.v4.runtime.CommonToken(oldToken) with CaretToken
        token.setType(CaretToken.CARET_TOKEN_TYPE)
        token.setOriginalToken(oldToken)
        token
    }
}

trait CaretToken extends org.antlr.v4.runtime.CommonToken with LazyLogging {

    private var originalToken: Option[Token] = None

    def setOriginalToken(token: Token): Unit = {
        originalToken = Some(token)
    }
    def getOriginalToken: Option[Token] = {
        originalToken
    }

    def getOriginalType: Int = {
        originalToken match {
            case Some(token) => token.getType
            case None => - 1
        }
    }
    var prevToken: Token = null

    private var caret: Option[Caret] = None

    def setCaret(caret: Caret): Unit = {
        this.caret = Some(caret)
    }

    def getCaretPositionInLine:Int = caret match {
        case Some(_caret) => _caret.startIndex
        case None => -1
    }

    def getCaretLine:Int = caret match {
        case Some(_caret) => _caret.line
        case None => -1
    }

    /*
    def getCaretOffset:Int = caret match {
        case Some(_caret) => _caret.getOffset
        case None => -1
    }
    */

    def getCaret: Option[Caret] = caret

    /**
      * make sure that for token which parser put into Caret position we
      * do not return text of the token that starts *after* caret or ends *before* caret
      */
    override def getText: String = {

        getOriginalToken match {
            case Some(token) =>
                /*
                if (this.getCaretOffset >= 0 && token.getStartIndex < this.getCaretOffset && (token.getStopIndex +1) >= this.getCaretOffset) {
                    super.getText
                } else {
                    ""
                }
                */
                token.getText
            case None =>
                ""
        }
    }

}


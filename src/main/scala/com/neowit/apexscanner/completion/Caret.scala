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

import com.neowit.apexscanner.nodes.Position
import org.antlr.v4.runtime.Token

/**
  * Created by Andrey Gavrikov 
  */
abstract class Caret(val position: Position) {
    val line: Int = position.line
    val col: Int = position.col
    private var tokenType: String = ""

    def setType(path: String): Unit = {
        tokenType = path
    }

    def getType: String = {
        tokenType
    }

    def equals(node: Token): Boolean = {
        line == node.getLine && col == node.getCharPositionInLine
    }

    /**
      * check if caret is After given Token
      */
    def isAfter(token: Token): Boolean = {
        val tokenEnds = token.getCharPositionInLine + token.getText.length -1
        token.getLine < line || line == token.getLine && tokenEnds < col
    }
    /**
      * check if caret is Before given Token
      */
    def isBefore(token: Token): Boolean = {
        token.getLine > line || line == token.getLine && token.getCharPositionInLine > col
    }

    /**
      * check if caret is Inside given Token
      */
    def isInside(token: Token): Boolean = {
        val tokenEnds = token.getCharPositionInLine + token.getText.length
        line == token.getLine && token.getCharPositionInLine <= col && tokenEnds >= col
    }
}

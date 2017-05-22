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

package com.neowit.apexscanner.nodes

import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.tree.RuleNode

case class Position(line: Int, col: Int)
object Position {
    val INVALID_LOCATION = Position(-1, -1)
    val FALLTHROUGH_LOCATION = Position(-2, -2)
}

case class Range(start: Position, end: Position) {
    def getDebugInfo: String = {
        val text =
            if (start != end) {
                s"${start.line}, ${start.col} - ${end.line}, ${end.col}"
            } else {
                s"${start.line}, ${start.col}"
            }
        "(" + text + ")"
    }

    /**
      * check if given location is inside current LocationInterval
      * @param location - line/col to check for inclusion
      * @return true if given location is inside current LocationInterval
      */
    def includesLocation(location: Position): Boolean = {
        if (start.line > location.line || start.line == location.line && start.col > location.col) {
            return false
        }
        if (end.line < location.line || end.line == location.line && end.col < location.col) {
            return false
        }
        true
    }
}

object Range {
    val INVALID_LOCATION = Range(Position.INVALID_LOCATION, Position.INVALID_LOCATION)
    val FALLTHROUGH_LOCATION = Range(Position.FALLTHROUGH_LOCATION, Position.FALLTHROUGH_LOCATION)

    def apply(node: RuleNode): Range = {
        FALLTHROUGH_LOCATION
    }
    def apply(ctx: ParserRuleContext): Range = {
        val startToken = ctx.getStart
        val endToken = ctx.getStop
        val startPosition = Position(startToken.getLine, startToken.getCharPositionInLine)
        // when calculating endPosition take into account length of stop token
        val endPosition = Position(endToken.getLine, endToken.getCharPositionInLine + endToken.getStopIndex - endToken.getStartIndex + 1)
        Range(
            start = startPosition,
            end = endPosition
        )
    }
    def apply(node: org.antlr.v4.runtime.tree.TerminalNode): Range = {
        Range(
            start = Position(node.getSymbol.getLine, node.getSymbol.getCharPositionInLine),
            end = Position(node.getSymbol.getLine, node.getSymbol.getCharPositionInLine + node.getSymbol.getText.length -1)
        )
    }
}


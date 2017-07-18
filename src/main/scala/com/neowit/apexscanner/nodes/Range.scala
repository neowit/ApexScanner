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

case class Position(line: Int, col: Int) {
    def isBefore(p: Position): Boolean = {
        isBefore(p.line, p.col)
    }
    def isBefore(otherLine: Int, otherCol: Int): Boolean = {
        line < otherLine ||
        line == otherLine && col < otherCol
    }
    def getDistance(p: Position): Distance = {
        if (this.line == p.line) {
            Distance(0, this.col - p.col)
        }
        val lines = this.line - p.line
        val cols = this.col - p.col
        Distance(lines, cols)
    }
}
case class Distance(lines: Int, cols: Int)
object Distance {
    def min(d1: Distance, d2: Distance): Distance = {
        if (d1.lines == d2.lines) {
            if (d1.cols < d2.cols) d1 else d2
        } else {

            if (d1.lines < d2.lines) d1 else d2
        }
    }
}
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


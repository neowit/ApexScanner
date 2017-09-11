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

import org.antlr.v4.runtime.{ParserRuleContext, Token}
import org.antlr.v4.runtime.tree.RuleNode

case class Position(line: Int, col: Int) extends Ordered[Position]{
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

    override def compare(that: Position): Int = {
        if (this.isBefore(that)) {
            -1
        } else if (this == that) {
            0
        } else {
            1
        }
    }
}

object Position {
    val INVALID_LOCATION = Position(-1, -1)
    val FALLTHROUGH_LOCATION = Position(-2, -2)

    def apply(token: Token): Position = {
        new Position(token.getLine, token.getCharPositionInLine)
    }

    /**
      * given position inside inner document (e.g. SOQL statement)
      * and offset of the document in the outer file (e.g. in Apex class)
      * calculate absolute position in the outer document
      * @param position position inside inner document (e.g. inside SOQL statement)
      * @param offset position of inner document in the outer document
      * @return
      */
    def toAbsolutePosition(position: Position, offset: Option[Position]): Position = {
        Position(
            getAbsoluteLine(position, offset),
            getAbsoluteCharPositionInLine(position, offset)
        )
    }
    def getAbsoluteLine(position: Position, offset: Option[Position]): Int = {
        offset match {
            case Some(Position(offsetLine, _)) =>
                offsetLine + position.line - 1
            case None => position.line
        }
    }

    def getAbsoluteCharPositionInLine(position: Position, offset: Option[Position]): Int = {
        offset match {
            case Some(Position(_, offsetCol)) =>
                if (1 == position.line) {
                    offsetCol + position.col
                } else {
                    position.col
                }
            case None => position.col
        }
    }

    /**
      * convert absolute position (global position in the outer document)
      * to relative position (position inside and relative inner document)
      * @return
      */
    def toRelativePosition(position: Position, offset: Option[Position]): Position = {
        Position(
            getRelativeLine(position, offset),
            getRelativeCharPositionInLine(position, offset)
        )
    }

    def getRelativeLine(position: Position, offset: Option[Position]): Int = {
        offset match {
            case Some(Position(offsetLine, _)) =>
                position.line - offsetLine + 1
            case None => position.line
        }
    }

    def getRelativeCharPositionInLine(position: Position, offset: Option[Position]): Int = {
        offset match {
            case Some(Position(offsetLine, offsetCol)) =>
                if (position.line == offsetLine) {
                    position.col - offsetCol + 1
                } else {
                    position.col
                }
            case None => position.col
        }
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


/**
  *
  * @param start position where range starts
  * @param end position where range ends
  * @param offset if current range is relative then offset may be used to specify parent range
  *               and allow to find absolute position of current Range
  */
case class Range(start: Position, end: Position, offset: Position = Position(0, 0)) {
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
    def includesLocation(location: Position, ignoreOffset: Boolean): Boolean = {
        val startLine = if (ignoreOffset || offset.line < 1) start.line else (start.line - 1) + offset.line //-1 because lines start with 1
        val startCol = if (ignoreOffset) start.col else start.col + offset.col

        if (startLine > location.line || startLine == location.line && startCol > location.col) {
            return false
        }

        val endLine = if (ignoreOffset) end.line else end.line + offset.line
        val endCol = if (ignoreOffset) end.col else end.col + offset.col

        if (endLine < location.line || endLine == location.line && endCol < location.col) {
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

    def apply(ctx: ParserRuleContext, offsetPosition: Position): Range = {
        val startToken = ctx.getStart
        val endToken = ctx.getStop
        val startPosition = Position(startToken.getLine, startToken.getCharPositionInLine)
        // when calculating endPosition take into account length of stop token
        //val endPosition = Range(endToken, offsetPosition).end
        val endTokenStart = Position(endToken.getLine, endToken.getCharPositionInLine)
        val endPosition = getEndPosition(endTokenStart, endToken.getText)

        Range(
            start = startPosition,
            end = endPosition,
            offset = offsetPosition
        )

    }
    def apply(ctx: ParserRuleContext): Range = {
        apply(ctx, Position(0, 0))
    }

    def apply(node: org.antlr.v4.runtime.tree.TerminalNode, offsetPosition: Position): Range = {
        val start = Position(node.getSymbol.getLine, node.getSymbol.getCharPositionInLine)
        Range(
            start,
            end = getEndPosition(start, node.getSymbol.getText),
            offset = offsetPosition
        )
    }

    def apply(node: org.antlr.v4.runtime.tree.TerminalNode): Range = {
        apply(node, Position(0, 0))
    }

    def apply(token: org.antlr.v4.runtime.Token, offsetPosition: Position): Range = {
        val start = Position(token.getLine, token.getCharPositionInLine)
        Range(
            start,
            end = getEndPosition(start, token.getText),
            offset = offsetPosition
        )
    }

    def getEndPosition(token: Token): Position = {
        val start = Position(token.getLine, token.getCharPositionInLine)
        getEndPosition(start, token.getText)
    }
    /**
      * calculate end position of provided text
      * @param start position where text starts
      * @param text text to evaluate
      * @return
      */
    def getEndPosition(start: Position, text: String): Position = {
        if (null == text || text.isEmpty) {
            return start
        }

        val lines = text.split("\\r?\\n")
        val (_endLine, _endCol) =
            if (lines.length > 1) {
                val endLine = start.line + lines.length - 1
                val endCol = lines.last.length
                (endLine, endCol)
            } else {
                val endLine = start.line
                val endCol = start.col + text.length -1
                (endLine, endCol)
            }
        Position(_endLine, _endCol)
    }
}


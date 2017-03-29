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

package com.neowit.apex.nodes

import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.tree.RuleNode

case class Location(line: Int, col: Int)
object Location {
    val INVALID_LOCATION = Location(-1, -1)
    val FALLTHROUGH_LOCATION = Location(-2, -2)
}

case class LocationInterval(start: Location, end: Location) {
    def detDebugInfo: String = {
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
    def includesLocation(location: Location): Boolean = {
        if (start.line > location.line || start.line == location.line && start.col > location.col) {
            return false
        }
        if (end.line < location.line || end.line == location.line && end.col < location.col) {
            return false
        }
        true
    }
}

object LocationInterval {
    val INVALID_LOCATION = LocationInterval(Location.INVALID_LOCATION, Location.INVALID_LOCATION)
    val FALLTHROUGH_LOCATION = LocationInterval(Location.FALLTHROUGH_LOCATION, Location.FALLTHROUGH_LOCATION)

    def apply(node: RuleNode): LocationInterval = {
        FALLTHROUGH_LOCATION
    }
    def apply(ctx: ParserRuleContext): LocationInterval = {
        val startToken = ctx.getStart
        val endToken = ctx.getStop

        LocationInterval(
            start = Location(startToken.getLine, startToken.getCharPositionInLine),
            end = Location(endToken.getLine, endToken.getCharPositionInLine)
        )
    }
    def apply(node: org.antlr.v4.runtime.tree.TerminalNode): LocationInterval = {
        LocationInterval(
            start = Location(node.getSymbol.getLine, node.getSymbol.getStartIndex),
            end = Location(node.getSymbol.getLine, node.getSymbol.getStopIndex)
        )
    }
}


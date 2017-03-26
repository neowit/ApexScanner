package com.neowit.apex.nodes

import org.antlr.v4.runtime.ParserRuleContext

case class Location(line: Int, col: Int)
object Location {
    val INVALID_LOCATION = Location(-1, -1)
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
}

object LocationInterval {
    val INVALID_LOCATION = LocationInterval(Location.INVALID_LOCATION, Location.INVALID_LOCATION)

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


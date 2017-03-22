package com.neowit.apex.nodes

import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.tree.TerminalNode

case class Location(line: Int, col: Int)
object Location {
    val INVALID_LOCATION = Location(-1, -1)
}
case class LocationInterval(start: Location, end: Location)

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
    def apply(node: TerminalNode): LocationInterval = {
        LocationInterval(
            start = Location(node.getSymbol.getLine, node.getSymbol.getStartIndex),
            end = Location(node.getSymbol.getLine, node.getSymbol.getStopIndex)
        )
    }
}

object NullNode extends AstNode {
    override def locationInterval: LocationInterval = LocationInterval.INVALID_LOCATION

    override def nodeType: AstNodeType = EmptyNodeType
}

trait AstNode {

    private var _parent: Option[AstNode] = None
    private val children = new scala.collection.mutable.ListBuffer[AstNode]()

    def locationInterval:LocationInterval
    def nodeType: AstNodeType

    def setParent(parent: AstNode): AstNode = {
        _parent = Option(parent)
        parent
    }

    def getParent: Option[AstNode] = _parent

    def addChild(node: AstNode): AstNode = {
        if (EmptyNodeType != node.nodeType) {
            children += node
        }
        node
    }

    def getChildren[T <: AstNode](nodeType: AstNodeType, recursively: Boolean = false): Seq[T] = {
        val immediateChildren = children.filter(_.nodeType == nodeType)
        val allFoundChildren =
            if (recursively) {
                immediateChildren.flatMap(_.getChildren(nodeType, recursively))
            } else {
                immediateChildren
            }
        allFoundChildren.map(_.asInstanceOf[T])
    }
    def getChild[T <: AstNode](nodeType: AstNodeType, recursively: Boolean = false): Option[T] = {
        getChildren(nodeType, recursively).headOption.map(_.asInstanceOf[T])
    }

}

sealed trait AstNodeType

case object AnnotationParameterNodeType extends AstNodeType
case object ApexAnnotationNodeType extends AstNodeType
case object ApexClassNodeType extends AstNodeType
case object ApexDocNodeType extends AstNodeType
case object ApexInterfaceNodeType extends AstNodeType
case object ClassVariableNodeType extends AstNodeType
case object DataTypeNodeType extends AstNodeType
case object EmptyNodeType extends AstNodeType
case object ExpressionNodeType extends AstNodeType
case object MethodNodeType extends AstNodeType
case object MethodParameterNodeType extends AstNodeType
case object ModifierNodeType extends AstNodeType
case object VariableNodeType extends AstNodeType
case object IdentifierNodeType extends AstNodeType


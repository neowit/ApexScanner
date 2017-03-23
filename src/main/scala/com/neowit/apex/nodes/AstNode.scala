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

trait AstNode {

    private var _parent: Option[AstNode] = None
    private val _children = new scala.collection.mutable.ListBuffer[AstNode]()

    def locationInterval:LocationInterval
    def nodeType: AstNodeType

    def setParent(parent: AstNode): AstNode = {
        _parent = Option(parent)
        parent
    }

    def getParent: Option[AstNode] = _parent

    def addChild(node: AstNode): AstNode = {
        if (NullNodeType != node.nodeType) {
            _children += node
            node.setParent(this)
        }
        node
    }

    def children: Seq[AstNode] = _children

    def getChildren[T <: AstNode](nodeType: AstNodeType, recursively: Boolean = false): Seq[T] = {
        val immediateChildren = _children.filter(_.nodeType == nodeType)

        // in case any of the children represent a FallThroughNode, query their children one step further
        val fallThroughChildren =
            if (FallThroughNodeType != nodeType && !recursively) {
                _children.filter(_.nodeType == FallThroughNodeType).flatMap(_.getChildren[FallThroughNode](nodeType))
            } else {
                Nil
            }

        val allFoundChildren =
            if (recursively) {
                immediateChildren ++ immediateChildren.flatMap(_.getChildren(nodeType, recursively))
            } else {
                immediateChildren
            }

        val allChildren = fallThroughChildren ++ allFoundChildren

        allChildren.map(_.asInstanceOf[T])
    }
    def getChild[T <: AstNode](nodeType: AstNodeType, recursively: Boolean = false): Option[T] = {
        getChildren[T](nodeType, recursively).headOption
    }

    /**
      * used for debug purposes
      * @return textual representation of this node and its children
      */
    def getDebugInfo: String = "\n" + locationInterval.detDebugInfo + " " + nodeType + " => "
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
case object ExtendsNodeType extends AstNodeType
case object FallThroughNodeType extends AstNodeType
case object ExpressionNodeType extends AstNodeType
case object MethodNodeType extends AstNodeType
case object MethodParameterNodeType extends AstNodeType
case object ModifierNodeType extends AstNodeType
case object VariableNodeType extends AstNodeType
case object IdentifierNodeType extends AstNodeType
case object FinalNodeType extends AstNodeType
case object ImplementsInterfaceNodeType extends AstNodeType
case object TypeArgumentNodeType extends AstNodeType
case object TypeArgumentsNodeType extends AstNodeType
case object QualifiedNameNodeType extends AstNodeType


package com.neowit.apex.nodes

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
    def getDebugInfo: String = {
        val shift = List.fill(getLevelsDeep)("\t").mkString("")
        "\n" + shift + locationInterval.detDebugInfo + " " + nodeType + " => "
    }
    // used in getDebugInfo for visual shift of child level compared to parent
    private var levelsDeep: Int = -1
    protected def getLevelsDeep: Int = {
        if (levelsDeep < 0) {
            levelsDeep =
                getParent match {
                    case Some(p) => p.getLevelsDeep + 1
                    case None => 0
                }
        }
        levelsDeep
    }
}



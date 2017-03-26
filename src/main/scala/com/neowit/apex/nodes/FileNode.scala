package com.neowit.apex.nodes

import java.nio.file.Path

case class FileNode(file: Path, locationInterval: LocationInterval) extends AstNode {
    override def nodeType: AstNodeType = FileNodeType

    /**
      * used for debug purposes
      *
      * @return textual representation of this node and its children
      */
    override def getDebugInfo: String = "FILE: " + file.toString

    override protected def getLevelsDeep: Int = -1
}

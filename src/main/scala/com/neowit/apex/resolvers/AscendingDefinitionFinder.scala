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

package com.neowit.apex.resolvers

import java.nio.file.FileSystems

import com.neowit.apex.Project
import com.neowit.apex.ast.AstBuilder
import com.neowit.apex.nodes._

import scala.annotation.tailrec

/**
  * Created by Andrey Gavrikov
  *
  * attempts to find node defining expression at specified location
  * this is ASCENDING lookup, starts from the bottom and goes up
  * Use this to find definition in the same file as target node
  */
class AscendingDefinitionFinder() {

    def findDefinition(rootNode: AstNode, location: Position): Option[AstNode] = {
        // first find actual node which we need to find the definition for
        val nodeFinder = new NodeByLocationFinder(location)
        nodeFinder.findInside(rootNode) match {
          case Some(targetNode) =>
              findDefinitionInternal(targetNode, targetNode)
          case None =>
                None
        }
    }

    def findDefinition(target: AstNode, startNode: AstNode): Option[AstNode] = {
        findDefinitionInternal(target, startNode)
    }

    /**
      * starting from startNode go UP node hierarchy and look for potential definition of given target node
      * @param target node which definition we are trying to find
      * @param startNode lowest node to start from and go UP
      * @return
      */
    @tailrec
    private def findDefinitionInternal(target: AstNode, startNode: AstNode): Option[AstNode] = {
        // first find actual node which we need to find the definition for
        val targetName = target match {
            case t:IdentifierNode =>
                Option(QualifiedName(Array(t.name)))
            case _ =>
                None
        }
        target match {
            case n: LocalVariableNode => Option(n)
            case n: ClassVariableNode  => Option(n)
            case _ => startNode.getParent match {
              case Some(parent) =>
                  val definitionNode =
                      parent.findChild{
                          case child: HasTypeDefinition =>
                              targetName.exists(qName => child.qualifiedName.exists(_.endsWith(qName)))
                          case _ => false
                      }
                  definitionNode match {
                    case Some(_) => definitionNode
                    case None =>
                        // go higher in parents hierarchy
                        findDefinitionInternal(target, parent)
                  }
              case None => None
            }
        }
    }
}
object AscendingDefinitionFinder {
    def main(args: Array[String]): Unit = {
        val path = FileSystems.getDefault.getPath ("/Users/andrey/development/scala/projects/ApexScanner/GrammarTests/TypeFinder.cls")
        //val position = Position(18, 20) //int
        val position = Position(18, 25) //str
        val astBuilder = new AstBuilder(Project(path))
        astBuilder.build(path)

        astBuilder.getAst(path) match {
            case None =>
            // do nothing
            case Some(result) if result.fileScanResult.errors.nonEmpty =>
                println("ERRORS ENCOUNTERED")
                result.fileScanResult.errors.foreach(println(_))

            case Some(result) =>
                val rootNode = result.rootNode
                testDefinitionFinder(rootNode, position)
        }
    }
    def testDefinitionFinder(rootNode: AstNode, location: Position): Unit = {
        val finder = new AscendingDefinitionFinder()
        finder.findDefinition(rootNode, location)  match {
            case Some(node: HasTypeDefinition) =>
                println("FOUND: " + node)
                println("   type: " + node.getType.text)
            case Some(node) =>
                println("FOUND: " + node)
                println("WARNING - NOT IMPLEMENTED - this is not HasTypeDefinition node ")
            case _ => println("NOT FOUND")
        }
    }
}

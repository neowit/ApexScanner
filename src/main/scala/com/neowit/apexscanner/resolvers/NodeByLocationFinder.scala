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

package com.neowit.apexscanner.resolvers

import java.nio.file.FileSystems

import com.neowit.apexscanner.{FileBasedDocument, Project}
import com.neowit.apexscanner.ast._
import com.neowit.apexscanner.nodes.{AstNode, Position}

/**
  * Created by Andrey Gavrikov
  * find AstNode at specified location
  */
class NodeByLocationFinder(location: Position) extends AstVisitor {
    private var foundNode: Option[AstNode] = None

    override def visit(node: AstNode): Boolean = {
        if (node.range.includesLocation(location)) {
            foundNode = Option(node)
            true
        } else {
            false
        }
    }

    def findInside(rootNode: AstNode): Option[AstNode] = {
        new AstWalker().walk(rootNode, this)
        foundNode
    }
}

object NodeByLocationFinder {
    def main(args: Array[String]): Unit = {

        //val path = FileSystems.getDefault.getPath(args(0))
        val path = FileSystems.getDefault.getPath ("/Users/andrey/development/scala/projects/ApexScanner/GrammarTests/TypeFinder.cls")
        val document = FileBasedDocument(path)
        val position = Position(18, 25)

        val astBuilder = new AstBuilder(Option(Project(path)), ApexAstBuilderVisitor.VISITOR_CREATOR_FUN)
        astBuilder.build(document)

        astBuilder.getAst(document) match {
            case None =>
            // do nothing
            case Some(result) if result.fileScanResult.errors.nonEmpty =>
                println("ERRORS ENCOUNTERED")
                result.fileScanResult.errors.foreach(println(_))

            case Some(result) =>
                val rootNode = result.rootNode
                testLocationFinder(rootNode, position)
        }
        ()
    }

    def testLocationFinder(rootNode: AstNode, location: Position): Unit = {
        val finder = new NodeByLocationFinder(location)
        finder.findInside(rootNode)  match {
          case Some(node) => println("FOUND: " + node)
          case None => println("NOT FOUND")
        }
    }
}

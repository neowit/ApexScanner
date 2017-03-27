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

package com.neowit.apex

import java.nio.file.FileSystems

import com.neowit.apex.ast._
import com.neowit.apex.nodes.AstNode

/**
  * Created by Andrey Gavrikov 
  */
object FindUsages {
    def main(args: Array[String]): Unit = {

        val astBuilder = new AstBuilder
        val path = FileSystems.getDefault.getPath(args(0))
        astBuilder.build(path)

        astBuilder.getAst(path) match {
            case None =>
                // do nothing
            case Some(result) if result.fileScanResult.errors.nonEmpty =>
                println("ERRORS ENCOUNTERED")
                result.fileScanResult.errors.foreach(println(_))

            case Some(result) =>
                val rootNode = result.rootNode
                testFindMethod(rootNode)
        }
    }


    def testFindMethod(rootNode: AstNode): Unit = {
        //val findMethodVisitor = new FindMethodVisitor("nonCaller", List.empty)
        val findMethodVisitor = new FindMethodVisitor("nonCaller", List("integer", "list<String>"))
        new AstWalker().walk(rootNode, findMethodVisitor)
        println(findMethodVisitor.getFoundMethod.map(_.getDebugInfo).getOrElse("NOT FOUND"))
    }

    /*
    def testMethodUsages(rootNode: AstNode): Unit = {
        val findUsagesVisitor = new FindUsagesVisitor()
        new AstWalker().walk(rootNode, findUsagesVisitor)
    }
    */

}

class FindUsagesVisitor(target: AstNode) extends AstVisitor {
    override def visit(node: AstNode): Boolean = {
        ???
    }
}


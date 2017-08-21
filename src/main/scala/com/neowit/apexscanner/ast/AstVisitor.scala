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

package com.neowit.apexscanner.ast

import java.nio.file.FileSystems

import com.neowit.apexscanner.Project
import com.neowit.apexscanner.nodes.AstNode
import com.neowit.apexscanner.scanner.{ApexcodeScanner, DocumentScanResult, Scanner}
import com.neowit.apexscanner.scanner.actions.SyntaxChecker

/**
  * Created by Andrey Gavrikov 
  */
trait AstVisitor {
    def preVisit(node: AstNode): Unit = {}
    def visit(node: AstNode): Boolean
    def postVisit(node: AstNode): Unit = {}
}

class DebugVisitor extends AstVisitor {
    override def visit(node: AstNode): Boolean = {
        println(node.getDebugInfo)
        true
    }
}
object DebugVisitor {
    var project: Project = _
    def main(args: Array[String]): Unit = {
        import scala.concurrent.ExecutionContext.Implicits.global

        val scanner = new ApexcodeScanner(Scanner.defaultIsIgnoredPath, onEachFileScanResult, SyntaxChecker.errorListenerCreator)

        val path = FileSystems.getDefault.getPath(args(0))
        project = Project(path)
        scanner.scan(path)
        ()
    }
    def onEachFileScanResult(result: DocumentScanResult): DocumentScanResult = {
        val visitor = new ApexAstBuilderVisitor(Option(project), Option(result.document))
        val compileUnit = visitor.visit(result.parseContext)
        new AstWalker().walk(compileUnit, new DebugVisitor)
        result
    }
}
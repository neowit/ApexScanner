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

package com.neowit.apex.ast

import java.nio.file.FileSystems

import com.neowit.apex.scanner.actions.SyntaxChecker
import com.neowit.apex.scanner.{FileScanResult, Scanner}

object ASTBuilder {

    def main(args: Array[String]): Unit = {
        val scanner = new Scanner()

        val path = FileSystems.getDefault.getPath(args(0))
        scanner.scan(path, Scanner.defaultIsIgnoredPath, onEachFileScanResult, SyntaxChecker.errorListenerCreator)
    }
    def onEachFileScanResult(result: FileScanResult): Unit = {
        val visitor = new ASTBuilderVisitor(result.sourceFile)
        val compileUnit = visitor.visit(result.parseContext)
        new AstWalker().walk(compileUnit, new DebugVisitor)
    }
}

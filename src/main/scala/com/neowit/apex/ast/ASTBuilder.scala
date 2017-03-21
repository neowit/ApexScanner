package com.neowit.apex.ast

import java.nio.file.FileSystems

import com.neowit.apex.scanner.actions.SyntaxChecker
import com.neowit.apex.scanner.{FileScanResult, Scanner}

/**
  * Created by Andrey Gavrikov on 22/03/2017.
  */
object ASTBuilder {

    def main(args: Array[String]): Unit = {
        val scanner = new Scanner()

        val path = FileSystems.getDefault.getPath(args(0))
        scanner.scan(path, Scanner.defaultIsIgnoredPath, onEachResult, SyntaxChecker.errorListenerCreator)
    }
    def onEachResult(result: FileScanResult): Unit = {
        val visitor = new ASTBuilderVisitor
        visitor.visit(result.parseContext)
    }
}

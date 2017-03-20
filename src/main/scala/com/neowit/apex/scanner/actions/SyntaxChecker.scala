package com.neowit.apex.scanner.actions

import java.nio.file.Path

import com.neowit.apex.scanner._
import org.antlr.v4.runtime.{BaseErrorListener, RecognitionException, Recognizer}

object SyntaxChecker {
    def errorListenerCreator(file: Path): ApexErrorListener = {
        new SyntaxCheckerErrorListener(file)
    }
}
class SyntaxChecker {
    import SyntaxChecker._
    /**
      * Parse & Check syntax in files residing in specified path/location
      * @param path file or folder with eligible apex files to check syntax
      * @param isIgnoredPath - provide this function if path points to a folder and certain paths inside need to be ignored
      * @param onEachResult - provide this function if additional action is required when result for each individual file is available
      * @return sequence of syntax check results
      */
    def check(path: Path,
             isIgnoredPath: Path => Boolean,
             onEachResult: FileScanResult => Unit): Seq[SyntaxCheckResult] = {

        val resultBuilder = Seq.newBuilder[SyntaxCheckResult]

        def onFileCheckResult(result: FileScanResult):Unit = {
            val sourceFile = result.sourceFile
            val fileName = result.sourceFile.getName(sourceFile.getNameCount-1).toString
            val res = SyntaxCheckResult(sourceFile, result.errors)
            resultBuilder += res
            onEachResult(result)
        }
        val scanner = new Scanner
        scanner.scan(path, isIgnoredPath, onFileCheckResult, errorListenerCreator)
        resultBuilder.result()
    }
}

case class SyntaxError(offendingSymbol: scala.Any,
                       line: Int,
                       charPositionInLine: Int,
                       msg: String)

private class SyntaxCheckerErrorListener(file: Path) extends BaseErrorListener with ApexErrorListener{
    private val errorBuilder = Seq.newBuilder[SyntaxError]
    override def syntaxError(recognizer: Recognizer[_, _],
                             offendingSymbol: scala.Any,
                             line: Int, charPositionInLine: Int,
                             msg: String,
                             e: RecognitionException): Unit = {
        //super.syntaxError(recognizer, offendingSymbol, line, charPositionInLine, msg, e)
        val error = SyntaxError(offendingSymbol, line, charPositionInLine, msg)
        errorBuilder += error
        //assert(false, "\n" + file.toString + s"\n=> ($line, $charPositionInLine): " + msg)
    }
    def result(): Seq[SyntaxError] = errorBuilder.result()
}
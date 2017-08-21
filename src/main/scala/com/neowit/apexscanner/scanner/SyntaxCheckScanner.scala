package com.neowit.apexscanner.scanner

import java.nio.file.Path

import com.neowit.apexscanner.VirtualDocument
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.atn.PredictionMode



/**
  * Created by Andrey Gavrikov 
  */
class SyntaxCheckScanner(scanners: Seq[Scanner]) extends Scanner {
    assert(scanners.nonEmpty, "SyntaxCheckScanner.scanners - At least one scanner must be provided")

    private val _mainScanner: Scanner = scanners.head

    override def isIgnoredPath(path: Path): Boolean = _mainScanner.isIgnoredPath(path)

    override def onEachResult(result: DocumentScanResult):DocumentScanResult = _mainScanner.onEachResult(result)

    override def errorListenerFactory(document: VirtualDocument): ApexErrorListener = _mainScanner.errorListenerFactory(document)

    override def scan(document: VirtualDocument, predictionMode: PredictionMode,
                      documentTokenStreamOpt: Option[CommonTokenStream]): DocumentScanResult = {
        val initialResult = _mainScanner.scan(document, predictionMode, None)
        var totalResult: DocumentScanResult = initialResult

        for (scanner <- scanners.tail) {
            val result = scanner.scan(document, predictionMode, Option(initialResult.tokenStream))
            totalResult = totalResult.copy(errors = totalResult.errors ++ result.errors)

        }
        totalResult
    }
}

package com.neowit.apexscanner.scanner

import java.io.File
import java.nio.file.{FileSystems, Files, Path}

import com.neowit.apexscanner.{TestConfigProvider, TextBasedDocument}
import com.neowit.apexscanner.antlr.ApexcodeLexer
import com.neowit.apexscanner.scanner.actions.SyntaxChecker
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.atn.PredictionMode
import org.scalatest.FunSuite
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}

import scala.collection.mutable
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

//import scala.collection.JavaConverters._

class SyntaxCheckerTest extends FunSuite with TestConfigProvider with ScalaFutures with IntegrationPatience {

    // TEST OUTPUT CONFIGURATION
    private val failOnError = true
    private val printSuccessfulFileNames = true
    // END TEST OUTPUT CONFIGURATION

    //private val matcher = FileSystems.getDefault.getPathMatcher("glob:*.cls")
    private val projectPath = getProperty("SyntaxCheckerTest.path")



    private val ignoredNames = Set(
        "A-Fake-Class.cls", // not real apex code
        "IObjectWrapper.cls", // old class, contains no longer supported type parameter: interface Name<T>
        "CompletionTester.cls" // this class usually contains broken syntax because of incomplete expressions
    )
    private val ignoredDirs = Set("resources_unpacked", "Referenced Packages", "_ProjectTemplate")

    private val processedKeys: mutable.HashSet[String] = new mutable.HashSet[String]()
    private var _testedApexFileCount = 0
    private var _testedSoqlCount = 0

    private def recordProcessedFile(path: Path): Unit = {
        processedKeys += getFileKey(path.toFile)
    }
    private def isDuplicate(path: Path): Boolean = {
        val file = path.toFile
        file.isFile && processedKeys.contains(getFileKey(file))
    }
    private def getFileKey(file: File): String = {
        val size = file.length()
        val key = file.getName + size
        key
    }
    def isIgnoredPath(path: Path): Boolean = {
        val isDirectory = Files.isDirectory(path)
        val fileName = path.toFile.getName
        if (isDirectory) {
            return ignoredDirs.contains(fileName)
        } else {
            //regular file
            if (
                (fileName.endsWith(".cls") || fileName.endsWith(".trigger"))
                && !fileName.contains("__") // exclude classes with namespace <Namespace>__classname.cls, they do not have apex code
                && !ignoredNames.contains(fileName) && !isDuplicate(path)) {
                return false // not ignored file
            }
        }
        true
    }

    test("test syntax of sample apex classes") {
        val checker = new SyntaxChecker()
        val path = FileSystems.getDefault.getPath(projectPath)
        val fileNameSetBuilder = Set.newBuilder[String]


        def onSoqlScanResult(scanResult: FileScanResult):Unit = {
            val file: Path = scanResult.document.file
            val soqlStr = scanResult.document.getTextContent.getOrElse("")
            val errors = scanResult.errors
            val fileName = file.getName(file.getNameCount-1).toString
            //fileNameSetBuilder += fileName
            if (errors.nonEmpty) {
                println("\n\n# successful SOQL statements: " + _testedSoqlCount)
                if (failOnError) {
                    print("Checked " + fileName)
                    errors.foreach(e =>  fail("\n" + file.toString + s"\n=> (${e.line}, ${e.charPositionInLine}): " + e.msg))
                } else {
                    // only report SOQL error, but not fail the whole test
                    print("Checked " + fileName)
                    print("\n     Error in Soql: \n" + soqlStr)
                }
            } else {
                _testedSoqlCount += 1
            }
        }

        val soqlScanner = new SoqlScanner(
            p => true,
            onEachResult = onSoqlScanResult,
            errorListenerFactory = SyntaxChecker.errorListenerCreator
        )

        def onApexFileCheckResult(scanResult: FileScanResult):Unit = {
            val file: Path = scanResult.document.file
            recordProcessedFile(file)
            val errors = scanResult.errors
            val fileName = file.getName(file.getNameCount-1).toString
            fileNameSetBuilder += fileName
            var printNewLine = false
            if (errors.nonEmpty) {
                if (failOnError) {
                    print("Checked " + fileName)
                    errors.foreach(e => fail("\n" + file.toString + s"\n=> (${e.line}, ${e.charPositionInLine}): " + e.msg))
                    printNewLine = true
                } else {
                    // only report an error, but not fail the whole test
                    print("Checked " + fileName)
                    errors.foreach(e => println("  \n" + file.toString + s"\n=> (${e.line}, ${e.charPositionInLine}): " + e.msg))
                    printNewLine = true
                }
            } else if (printSuccessfulFileNames) {
                print("Checked " + fileName)
                printNewLine = true
            }
            // test SOQL
            testSoqlStatements(soqlScanner, scanResult)

            if (printNewLine) {
                println() // new line
            }
            _testedApexFileCount += 1
            if (0 == _testedApexFileCount % 100) {
                println("# Tested apex file count: " + _testedApexFileCount)
            }
        }

        val start = System.currentTimeMillis
        val res = checker.check(path, isIgnoredPath, onApexFileCheckResult)
        Await.result(res, Duration.Inf)
        val diff = System.currentTimeMillis - start
        println("==================================================")
        val fileNameSet = fileNameSetBuilder.result()
        println(s"Total ${fileNameSet.size} unique file names tested (actual number of tested files may be much higher)")
        println("# successful SOQL statements: " + _testedSoqlCount)
        println("# Time taken: " + diff / 1000.0 +  "s")
    }

    private def testSoqlStatement(soqlScanner: SoqlScanner, file: Path, soqlStr: String): Unit = {
        //println("SOQL: " + soqlStr)
        val scanResult = soqlScanner.scan(TextBasedDocument(soqlStr, file), PredictionMode.SLL)
        soqlScanner.onEachResult(scanResult)
    }
    private def testSoqlStatements(soqlScanner: SoqlScanner, scanResult: FileScanResult): Unit = {
        var count = 0
        val file = scanResult.document.getFileName
        getSoqlStatements(scanResult.tokenStream).foreach{soqlStr =>
            testSoqlStatement(soqlScanner, file, soqlStr)
            count += 1
        }
        if (count > 0) {
            if (scanResult.errors.isEmpty && printSuccessfulFileNames) {
                print("; SOQL=" + count)
            }
        }
    }

    private def getSoqlStatements(tokenStream: CommonTokenStream): List[String] = {
        val listBuilder = List.newBuilder[String]
        for ( i <- Range(0, tokenStream.getNumberOfOnChannelTokens) ) {
            val token = tokenStream.get(i)
            if (ApexcodeLexer.SoqlLiteral == token.getType) {
                listBuilder += token.getText
            }
        }
        listBuilder.result()
    }

}

package com.neowit.apexscanner.scanner

import java.io.File
import java.nio.file.{FileSystems, Files, Path}

import com.neowit.apexscanner.{TestConfigProvider, VirtualDocument}
import com.neowit.apexscanner.scanner.actions.SyntaxChecker
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatest.funsuite.AnyFunSuite

import scala.collection.mutable
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

//import scala.collection.JavaConverters._

class SyntaxCheckerTest extends AnyFunSuite with TestConfigProvider with ScalaFutures with IntegrationPatience {self =>

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
    private def ignoredDirs = {
        val dirs = getProperty("SyntaxCheckerTest.ignoreDirs") // semicolon separated values
        val configuredDirs: Set[String] =
        if (null != dirs) {
            dirs.split(";").map(_.trim).toSet //split by ';' and remove spaces before/after each value
        } else {
            Set()
        }
        Set("resources_unpacked", "Referenced Packages", "_ProjectTemplate") ++ configuredDirs
    }

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
    private def isIgnoredPath(path: Path): Boolean = {
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
        val path = FileSystems.getDefault.getPath(projectPath)
        val fileNameSetBuilder = Set.newBuilder[String]

        var countOfSoqlStatementsInFile = 0

        def onSoqlScanResult(scanResult: DocumentScanResult):DocumentScanResult = {
            val file: Path = scanResult.document.fileOpt.get
            val soqlStr = scanResult.document.getTextContent.getOrElse("")
            val errors = scanResult.errors
            val fileName = file.getName(file.getNameCount-1).toString
            countOfSoqlStatementsInFile += 1
            //fileNameSetBuilder += fileName
            if (errors.nonEmpty) {
                println("\n\n# successful SOQL statements: " + _testedSoqlCount)
                if (failOnError) {
                    print("Checked " + fileName)
                    print("\n     Error in Soql: \n" + soqlStr)
                    errors.foreach(e =>  fail("\n" + file.toString + s"\n=> (${e.line}, ${e.charPositionInLine}): " + e.msg))
                } else {
                    // only report SOQL error, but not fail the whole test
                    print("Checked " + fileName)
                    print("\n     Error in Soql: \n" + soqlStr)
                }
            } else {
                _testedSoqlCount += 1
            }
            scanResult
        }

        val soqlScanner = new SoqlScanner() {
            override def isIgnoredPath(path: Path): Boolean = true
            override def onEachResult(result: DocumentScanResult): DocumentScanResult = onSoqlScanResult(result)
            override def errorListenerFactory(document: VirtualDocument): ApexErrorListener = SyntaxChecker.errorListenerCreator(document)
        }

        def onApexFileCheckResult(scanResult: DocumentScanResult):DocumentScanResult = {
            val file: Path = scanResult.document.fileOpt.get
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
                if (countOfSoqlStatementsInFile > 0) {
                    print("; SOQL=" + countOfSoqlStatementsInFile)
                }
                printNewLine = true
            }
            // test SOQL
            //testSoqlStatements(soqlScanner, scanResult)

            if (printNewLine) {
                println() // new line
            }
            _testedApexFileCount += 1
            if (0 == _testedApexFileCount % 100) {
                println("# Tested apex file count: " + _testedApexFileCount)
            }
            countOfSoqlStatementsInFile = 0
            scanResult
        }
        val apexcodeScanner = new ApexcodeScanner( ) {
            override def isIgnoredPath(path: Path): Boolean = self.isIgnoredPath(path)

            override def onEachResult(result: DocumentScanResult): DocumentScanResult = onApexFileCheckResult(result)

            override def errorListenerFactory(document: VirtualDocument): ApexErrorListener = SyntaxChecker.errorListenerCreator(document)
        }

        val checker = new SyntaxCheckScanner(Seq(apexcodeScanner, soqlScanner))
        //val checker = new SyntaxChecker(scanner)


        val start = System.currentTimeMillis
        val res = checker.scan(path).map(ignore => checker.getResult)
        Await.result(res, Duration.Inf)
        val diff = System.currentTimeMillis - start
        println("==================================================")
        val fileNameSet = fileNameSetBuilder.result()
        println(s"Total ${fileNameSet.size} unique file names tested (actual number of tested files may be much higher)")
        println("# successful SOQL statements: " + _testedSoqlCount)
        println("# Time taken: " + diff / 1000.0 +  "s")
    }

}

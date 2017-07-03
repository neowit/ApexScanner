package com.neowit.apexscanner.scanner

import java.nio.file.{FileSystems, Files, Path}

import com.neowit.apexscanner.TestConfigProvider
import com.neowit.apexscanner.scanner.actions.SyntaxChecker
import org.scalatest.FunSuite
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

class SyntaxCheckerTest extends FunSuite with TestConfigProvider with ScalaFutures with IntegrationPatience {

    //private val matcher = FileSystems.getDefault.getPathMatcher("glob:*.cls")
    private val projectPath = getProperty("SyntaxCheckerTest.path")



    private val ignoredNames = Set(
        "A-Fake-Class.cls", // not real apex code
        "IObjectWrapper.cls", // old class, contains no longer supported type parameter: interface Name<T>
        "CompletionTester.cls" // this class usually contains broken syntax because of incomplete expressions
    )
    private val ignoredDirs = Set("resources_unpacked", "Referenced Packages", "_ProjectTemplate")

    def isIgnoredPath(path: Path): Boolean = {
        val isDirectory = Files.isDirectory(path)
        val fileName = path.getName(path.getNameCount-1).toString
        if (isDirectory) {
            return ignoredDirs.contains(fileName)
        } else {
            //regular file
            if (
                (fileName.endsWith(".cls") || fileName.endsWith(".trigger"))
                && !fileName.contains("__") // exclude classes with namespace <Namespace>__classname.cls, they do not have apex code
                && !ignoredNames.contains(fileName)) {
                return false // not ignored file
            }
        }
        true
    }

    test("test syntax of sample apex classes") {
        val checker = new SyntaxChecker()
        val path = FileSystems.getDefault.getPath(projectPath)
        val fileNameSetBuilder = Set.newBuilder[String]

        def onFileCheckResult(scanResult: FileScanResult):Unit = {
            val file: Path = scanResult.document.file
            val errors = scanResult.errors
            val fileName = file.getName(file.getNameCount-1).toString
            fileNameSetBuilder += fileName
            println("Checking " + fileName)
            errors.foreach(e =>  assert(false, "\n" + file.toString + s"\n=> (${e.line}, ${e.charPositionInLine}): " + e.msg))
        }

        val start = System.currentTimeMillis
        val res = checker.check(path, isIgnoredPath, onFileCheckResult)
        Await.result(res, Duration.Inf)
        val diff = System.currentTimeMillis - start
        println("==================================================")
        val fileNameSet = fileNameSetBuilder.result()
        println(s"Total ${fileNameSet.size} unique file names tested (actual number of tested files may be much higher)")
        println("# Time taken: " + diff / 1000.0 +  "s")
    }
}

package com.neowit.apex.scanner

import java.nio.file.{FileSystems, Files, Path}

import com.neowit.apex.TestConfigProvider
import com.neowit.apex.scanner.actions.SyntaxChecker
import org.scalatest.FunSuite

class SyntaxCheckerTest extends FunSuite with TestConfigProvider {

    //private val matcher = FileSystems.getDefault.getPathMatcher("glob:*.cls")
    private val projectPath = getProperty("SyntaxCheckerTest.path")



    private val ignoredNames = Set(
        "A-Fake-Class.cls", // not real apex code
        "IObjectWrapper.cls" // old class, contains no longer supported type parameter: interface Name<T>
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
            val file: Path = scanResult.sourceFile
            val errors = scanResult.errors
            val fileName = file.getName(file.getNameCount-1).toString
            fileNameSetBuilder += fileName
            println("Checking " + fileName)
            errors.foreach(e =>  assert(false, "\n" + file.toString + s"\n=> (${e.line}, ${e.charPositionInLine}): " + e.msg))
        }

        val start = System.currentTimeMillis
        checker.check(path, isIgnoredPath, onFileCheckResult)
        val diff = System.currentTimeMillis - start
        println("==================================================")
        val fileNameSet = fileNameSetBuilder.result()
        println(s"Total ${fileNameSet.size} unique file names tested (actual number of tested files is much higher)")
        println("# Time taken: " + diff / 1000.0 +  "s")
    }
}

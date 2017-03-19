package com.neowit.apex.scanner.antlr

import java.io.FileInputStream
import java.nio.file.attribute.BasicFileAttributes
import java.nio.file._
import java.util.Properties

import org.antlr.v4.runtime._
import org.scalatest._
/**
  * Created by Andrey Gavrikov on 15/03/2017.
  */
class GrammarTest extends FunSuite {
    private val is = getClass.getClassLoader.getResource("paths.properties").openStream()
    val paths = new Properties()
    paths.load(is)

    //private val matcher = FileSystems.getDefault.getPathMatcher("glob:*.cls")
    private val projectPath = paths.getProperty("grammar-tests.path")

    test("Test All Apex Class Examples") {
        val path = FileSystems.getDefault.getPath(projectPath)
        val fileListBuilder = List.newBuilder[Path]
        val ignoredNames = Set(
            "A-Fake-Class.cls", // not real apex code
            "IObjectWrapper.cls" // old class, contains no longer supported type parameter: interface Name<T>
        )
        val ignoredDirs = Set("resources_unpacked", "Referenced Packages", "_ProjectTemplate")

        val classFileVisitor = new SimpleFileVisitor[Path]() {
            override def visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult = {
                val fileName = file.getName(file.getNameCount-1).toString
                if (!attrs.isDirectory
                    && fileName.endsWith(".cls")
                    && !fileName.contains("__") // exclude classes with namespace <Namespace>__classname.cls, they do not have apex code
                    && !ignoredNames.contains(fileName)) {
                    fileListBuilder += file
                }
                FileVisitResult.CONTINUE
            }

            override def preVisitDirectory(dir: Path, attrs: BasicFileAttributes): FileVisitResult = {
                val dirName = dir.getName(dir.getNameCount-1).toString
                if (ignoredDirs.contains(dirName)) {
                    FileVisitResult.SKIP_SUBTREE
                } else {
                    super.preVisitDirectory(dir, attrs)
                }
            }
        }
        Files.walkFileTree( path, classFileVisitor)
        val files = fileListBuilder.result()

        val fileNameSetBuilder = Set.newBuilder[String]
        //Files.list(p).filter(_.toString.endsWith(".cls")).forEach{file =>
        files.foreach{ file =>
            val lexer = getLexer(file)
            val fileName = file.getName(file.getNameCount-1).toString
            println("Checking " + fileName)
            fileNameSetBuilder += fileName
            val tokens = new CommonTokenStream(lexer)
            val parser = new ApexcodeParser(tokens)
            parser.addErrorListener(new SyntaxErrorListener(file))
            val tree = parser.compilationUnit()
        }
        println("==================================================")
        val fileNameSet = fileNameSetBuilder.result()
        println(s"Total ${fileNameSet.size} unique file names tested (actual number of tested files is much higher)")
    }

    /**
      * default case insensitive ApexCode lexer
      * @param file - file to parse
      * @return case insensitive ApexcodeLexer
      */
    def getLexer(file: Path): ApexcodeLexer = {
        //val input = new ANTLRInputStream(new FileInputStream(file))
        val input = new CaseInsensitiveInputStream(new FileInputStream(file.toFile))
        val lexer = new ApexcodeLexer(input)
        lexer
    }

}

class SyntaxErrorListener(file: Path) extends BaseErrorListener {
    override def syntaxError(recognizer: Recognizer[_, _],
                             offendingSymbol: scala.Any,
                             line: Int, charPositionInLine: Int,
                             msg: String,
                             e: RecognitionException): Unit = {
        //super.syntaxError(recognizer, offendingSymbol, line, charPositionInLine, msg, e)
        assert(false, "\n" + file.toString + s"\n=> ($line, $charPositionInLine): " + msg)
    }
}
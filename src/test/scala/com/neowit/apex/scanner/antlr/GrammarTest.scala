package com.neowit.apex.scanner.antlr

import java.io.FileInputStream
import java.nio.file.{FileSystems, Files, Path}
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
        val p = FileSystems.getDefault.getPath(projectPath)
        Files.list(p).filter(_.toString.endsWith(".cls")).forEach{file =>
            val lexer = getLexer(file)
            val tokens = new CommonTokenStream(lexer)
            val parser = new ApexcodeParser(tokens)
            parser.addErrorListener(new SyntaxErrorListener(file))
            val tree = parser.compilationUnit()
        }
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
        assert(false, file.toFile.getName + "=> " + msg)
    }
}
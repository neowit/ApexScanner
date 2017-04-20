/*
 * Copyright (c) 2017 Andrey Gavrikov.
 *
 * This file is part of https://github.com/neowit/apexscanner
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.neowit.apexscanner.scanner

import java.nio.file.attribute.BasicFileAttributes
import java.nio.file.{FileVisitResult, Files, Path, SimpleFileVisitor}

import com.neowit.apexscanner.antlr.{ApexcodeLexer, ApexcodeParser}
import com.neowit.apexscanner.scanner.actions.SyntaxError
import org.antlr.v4.runtime._

case class FileScanResult(sourceFile: Path, errors: Seq[SyntaxError], parseContext: ParserRuleContext)

object Scanner{

    private[Scanner] def emptyOnEachResult(result: FileScanResult): Unit = ()

    private val ignoredDirs = Set("resources_unpacked", "Referenced Packages")
    def defaultIsIgnoredPath(path: Path): Boolean = {
        val isDirectory = Files.isDirectory(path)
        val fileName = path.getName(path.getNameCount-1).toString
        if (isDirectory) {
            return ignoredDirs.contains(fileName)
        } else {
            //regular file
            if (
                (fileName.endsWith(".cls") || fileName.endsWith(".trigger"))
                    && !fileName.contains("__") // exclude classes with namespace <Namespace>__classname.cls, they do not have apex code
            ) {
                return false // not ignored file
            }
        }
        true
    }
}

/**
  * Parse & Check syntax in files residing in specified path/location
  * @param isIgnoredPath - provide this function if path points to a folder and certain paths inside need to be ignored
  * @param onEachResult - provide this function if additional action is required when result for each individual file is available
  * @param errorListenerFactory - factory method creating syntax error listener
  * @return
  */
class Scanner(isIgnoredPath: Path => Boolean = Scanner.defaultIsIgnoredPath,
              onEachResult: FileScanResult => Unit = Scanner.emptyOnEachResult,
              errorListenerFactory: Path => ApexErrorListener) {

    /**
      * Parse & Check syntax in files residing in specified path/location
      * @param path file or folder with eligible apex files to check syntax
      * @return
      */
    def scan(path: Path): Unit = {

        val fileListBuilder = List.newBuilder[Path]

        val apexFileVisitor = new SimpleFileVisitor[Path]() {
            override def visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult = {
                if (!attrs.isDirectory && !isIgnoredPath(file) ) {
                    fileListBuilder += file
                }
                FileVisitResult.CONTINUE
            }

            override def preVisitDirectory(dir: Path, attrs: BasicFileAttributes): FileVisitResult = {
                if (isIgnoredPath(dir)) {
                    FileVisitResult.SKIP_SUBTREE
                } else {
                    super.preVisitDirectory(dir, attrs)
                }
            }
        }

        Files.walkFileTree( path, apexFileVisitor)
        val files = fileListBuilder.result()

        files.foreach{ file =>
            val lexer = getLexer(file)
            val tokens = new CommonTokenStream(lexer)
            val parser = new ApexcodeParser(tokens)
            //val errorBuilder = Seq.newBuilder[SyntaxError]
            val errorListener = errorListenerFactory(file)
            //parser.addErrorListener(new SyntaxCheckerErrorListener(file, errorBuilder))
            parser.addErrorListener(errorListener)

            // run actual scan
            val compilationUnit:ParserRuleContext = parser.compilationUnit()

            val errors = errorListener.result()
            onEachResult(FileScanResult(file, errors, compilationUnit))

        }
    }

    /**
      * default case insensitive ApexCode lexer
      * @param file - file to parse
      * @return case insensitive ApexcodeLexer
      */
    def getLexer(file: Path): ApexcodeLexer = {
        //val input = new ANTLRInputStream(new FileInputStream(file))
        //val input = new CaseInsensitiveInputStream(new FileInputStream(file.toFile))
        // since Antlr 4.7 ANTLRInputStream is deprecated
        val input = CharStreams.fromPath(file)
        val lexer = new ApexcodeLexer(input)
        lexer
    }
}



/*
 * Copyright (c) 2017 Andrey Gavrikov.
 *
 * This file is part of https://github.com/neowit/apexscanner
 *
 *  This file is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  This file is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 */

package com.neowit.apexscanner.antlr

import java.nio.charset.StandardCharsets

import com.neowit.apexscanner.VirtualDocument
import org.antlr.v4.runtime.{CharStreams, ConsoleErrorListener, Parser}
/**
  * Created by Andrey Gavrikov 
  */
object ApexParserUtils {
    /**
      * in most cases there is no need to dump syntax errors into console
      * @param parser - ApexcodeParser from which to remove console error listener
      */
    def removeConsoleErrorListener(parser: Parser): Unit = {
        parser.getErrorListeners.removeIf(p => p.isInstanceOf[ConsoleErrorListener])
        ()
    }
    /**
      * default case insensitive ApexCode lexer
      * @param document - file to parse
      * @return case insensitive ApexcodeLexer
      */
    def getDefaultLexer(document: VirtualDocument): ApexcodeLexer = {
        //val input = new ANTLRInputStream(new FileInputStream(file))
        //val input = new CaseInsensitiveInputStream(new FileInputStream(file.toFile))
        // since Antlr 4.7 ANTLRInputStream is deprecated
        //val input = CharStreams.fromPath(file)
        val input = CharStreams.fromStream(document.inputStream, StandardCharsets.UTF_8)
        val lexer = new ApexcodeLexer(input)
        lexer
    }
}

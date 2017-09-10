/*
 *
 *  * Copyright (c) 2017 Andrey Gavrikov.
 *  * this file is part of tooling-force.com application
 *  * https://github.com/neowit/tooling-force.com
 *  *
 *  * This program is free software: you can redistribute it and/or modify
 *  * it under the terms of the GNU Lesser General Public License as published by
 *  * the Free Software Foundation, either version 3 of the License, or
 *  * (at your option) any later version.
 *  *
 *  * This program is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  * GNU Lesser General Public License for more details.
 *  *
 *  * You should have received a copy of the GNU Lesser General Public License
 *  * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.neowit.apexscanner.antlr

import com.neowit.apexscanner.VirtualDocument
import org.antlr.v4.runtime.{ANTLRErrorStrategy, BailErrorStrategy, CommonTokenStream}

/**
  * Created by Andrey Gavrikov 
  */
object SoqlParserUtils {
    /**
      * @return ApexcodeParser based on CommonTokenStream
      */
    def createParserWithCommonTokenStream(document: VirtualDocument,
                                          errorHandlerOpt: Option[ANTLRErrorStrategy] = Option(new BailErrorStrategy)
                                         ): (SoqlParser, CommonTokenStream) = {
        val lexer = new SoqlLexer(document.getCharStream)
        val tokens = new CommonTokenStream(lexer)
        val parser = new SoqlParser(tokens)
        // do not dump parse errors into console (or any other default listeners)
        parser.removeErrorListeners()
        errorHandlerOpt.foreach(parser.setErrorHandler)
        //parser.getInterpreter.setPredictionMode(PredictionMode.SLL)
        //parser.getInterpreter.setPredictionMode(PredictionMode.LL)
        (parser, tokens)
    }
}

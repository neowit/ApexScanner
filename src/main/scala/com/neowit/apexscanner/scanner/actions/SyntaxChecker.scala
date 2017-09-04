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

package com.neowit.apexscanner.scanner.actions


import com.neowit.apexscanner.{TokenBasedDocument, VirtualDocument}
import com.neowit.apexscanner.nodes.Position
import com.neowit.apexscanner.scanner._
import org.antlr.v4.runtime._


object SyntaxChecker {
    def errorListenerCreator(document: VirtualDocument): ApexErrorListener = {
        new SyntaxCheckerErrorListener(document)
    }

}


/**
  *
  * @param offset if document is inside another document (e.g. SOQL statement inside Apex class)
  *               then error position in the outer document needs to be adjusted
  */
case class SyntaxError(offendingSymbol: scala.Any,
                       line: Int,
                       charPositionInLine: Int,
                       msg: String,
                       offset: Option[Position] = None
                      ) {

    def getAdjustedLine: Int = {
        offset match {
            case Some(Position(offsetLine, _)) =>
                offsetLine + line - 1
            case None => line
        }
    }

    def getAdjustedCharPositionInLine: Int = {
        offset match {
            case Some(Position(_, offsetCol)) =>
                if (1 == line) {
                    offsetCol + charPositionInLine
                } else {
                    charPositionInLine
                }
            case None => charPositionInLine
        }
    }
}

private class SyntaxCheckerErrorListener(document: VirtualDocument) extends BaseErrorListener with ApexErrorListener {
    private val errorBuilder = Seq.newBuilder[SyntaxError]

    override def syntaxError(recognizer: Recognizer[_, _],
                             offendingSymbol: scala.Any,
                             line: Int, charPositionInLine: Int,
                             msg: String,
                             e: RecognitionException): Unit = {
        //super.syntaxError(recognizer, offendingSymbol, line, charPositionInLine, msg, e)
        // make sure msg does not include 'FIXER_TOKEN', end user has no use for it
        val msgCleaned = msg.replace("'FIXER_TOKEN', ", "")
        val error =
        document match {
            case _doc @ TokenBasedDocument(token, _, _) => // adjust error location
                val offset = Position(token.getLine, token.getCharPositionInLine)
                SyntaxError(offendingSymbol, line, charPositionInLine, msgCleaned, Option(offset))
            case _ => // return as is
                SyntaxError(offendingSymbol, line, charPositionInLine, msgCleaned)
        }
        errorBuilder += error
        //assert(false, "\n" + file.toString + s"\n=> ($line, $charPositionInLine): " + msg)
    }
    def result(): Seq[SyntaxError] = errorBuilder.result()

    /**
      * clear accumulated errors
      * @return
      */
    def clear(): Unit = {
        errorBuilder.clear()
    }
}
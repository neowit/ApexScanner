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

import java.nio.file.Path

import com.neowit.apexscanner.TextBasedDocument
import com.neowit.apexscanner.completion.CaretInDocument
import com.neowit.apexscanner.nodes.Position

/**
  * Created by Andrey Gavrikov 
  */
object CaretUtils {
    def getCaret(text: String, file: Path): CaretInDocument = {
        import scala.util.control.Breaks._
        var foundCaretPosition: CaretInDocument = null

        val caretTag = "<CARET>"
        val lines = text.split("""\n""")
        var lineNum = 1
        breakable {
            for (line <- lines) {
                val caretCharacterInLine = line.indexOf(caretTag)
                if (caretCharacterInLine >= 0) {
                    // found relevant line, remove caret tag from it
                    val documentText = text.replace(caretTag, "")
                    val fixedDocument = TextBasedDocument(documentText, Option(file), offset = None)
                    foundCaretPosition = new CaretInDocument(Position(lineNum, caretCharacterInLine), fixedDocument)
                    break() //FixMe - convert to code which does not require break
                }
                lineNum += 1
            }
        }

        if (null == foundCaretPosition) {
            throw new IllegalArgumentException(s"Caret not found in text $text")
        } else {
            foundCaretPosition
        }
    }
}

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

package com.neowit.apexscanner.symbols

import com.neowit.apexscanner.nodes.Location

/**
  * Created by Andrey Gavrikov
  *
  * Primary purpose of DocumentSymbol is to be displayed as a useful part of file structure
  * for example
  * - method is useful to display ina file structure
  * - anonymous code block or method variable are not useful parts of displayable file structure
  */
trait Symbol {
    def symbolName: String
    def symbolKind: SymbolKind
    def symbolLocation: Location
    def parentSymbol: Option[Symbol]
    def documentation: Option[String] = None
    def symbolIsStatic: Boolean
    def symbolValueType: Option[String]

    def isMethodLike: Boolean = false
    def methodParameters: Seq[String] = Seq.empty

    /**
      * The label of this completion item. By default
      * also the text that is inserted when selecting
      * this completion.
      */
    def symbolLabel: String = {
        val staticStr = if (symbolIsStatic) "static " else ""
        val paramStr =
            if (isMethodLike) {
                if (methodParameters.isEmpty) "()" else "(" + methodParameters.mkString(",") + ")"
            } else {
                ""
            }
        staticStr + symbolValueType.getOrElse("") + " " + symbolName + paramStr
    }
    /**
      * A string that should be inserted a document when selecting
      * this completion. When None the label is used.
      */
    def symbolInsertText: String = {
        val paramStr = if (isMethodLike) "()"  else  ""
        symbolName + paramStr
    }

    override def toString: String = symbolKind.getDebugInfo + ": " + symbolName + " => @ " + symbolLocation.getDebugInfo
}

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

/**
  * Created by Andrey Gavrikov
  *
  * symbol kinds are modeled after
  * interface SymbolKind from LSP:
  * https://github.com/Microsoft/language-server-protocol/blob/master/protocol.md
  */
trait SymbolKind {
    val code: Int
    def getDebugInfo: String
}
object SymbolKind {
    object File extends SymbolKind {
        override val code: Int = 1
        override def getDebugInfo: String = "File"
    }
    object Trigger extends SymbolKind {
        override val code: Int = File.code // this is not mistake, LSP does not support a notion of Trigger
        override def getDebugInfo: String = "Trigger"
    }
    object Class extends SymbolKind {
        override val code: Int = 5
        override def getDebugInfo: String = "Class"
    }
    object Method extends SymbolKind {
        override val code: Int = 6
        override def getDebugInfo: String = "Method"
    }
    object Property extends SymbolKind {
        override val code: Int = 7
        override def getDebugInfo: String = "Property"
    }
    object Annotation extends SymbolKind {
        override val code: Int = 7
        override def getDebugInfo: String = "Property"
    }
    object Field extends SymbolKind {
        override val code: Int = 8
        override def getDebugInfo: String = "Field"
    }
    object Constructor extends SymbolKind {
        override val code: Int = 9
        override def getDebugInfo: String = "Constructor"
    }
    object Enum extends SymbolKind {
        override val code: Int = 10
        override def getDebugInfo: String = "Enum"
    }
    object Interface extends SymbolKind {
        override val code: Int = 11
        override def getDebugInfo: String = "Interface"
    }
    object Variable extends SymbolKind {
        override val code: Int = 13
        override def getDebugInfo: String = "Variable"
    }

}

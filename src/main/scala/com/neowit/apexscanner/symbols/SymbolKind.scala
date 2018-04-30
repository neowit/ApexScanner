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
  * namespace CompletionItemKind from LSP:
  * https://microsoft.github.io/language-server-protocol/specification#textDocument_completion
  */
trait SymbolKind {
    val code: Int
    val letter: Char
    def getDebugInfo: String
}
object SymbolKind {
    object File extends SymbolKind {
        override val code: Int = 17
        override val letter: Char = 'F'
        override def getDebugInfo: String = "File"
    }
    object Trigger extends SymbolKind {
        override val code: Int = File.code // this is not mistake, LSP does not support a notion of Trigger
        override val letter: Char = 't'
        override def getDebugInfo: String = "Trigger"
    }
    object Class extends SymbolKind {
        override val code: Int = 7
        override val letter: Char = 'C'
        override def getDebugInfo: String = "Class"
    }
    object Constant extends SymbolKind {
        override val code: Int = 21
        override val letter: Char = 'c'
        override def getDebugInfo: String = "Constant"
    }
    object SObject extends SymbolKind {
        override val code: Int = 7
        override val letter: Char = 'O'
        override def getDebugInfo: String = "SObject"
    }
    object Method extends SymbolKind {
        override val code: Int = 2
        override val letter: Char = 'm'
        override def getDebugInfo: String = "Method"
    }
    object Property extends SymbolKind {
        override val code: Int = 10
        override val letter: Char = 'p'
        override def getDebugInfo: String = "Property"
    }
    object Annotation extends SymbolKind {
        override val code: Int = 10 // LSP does nto support Kind: annotation
        override val letter: Char = 'a'
        override def getDebugInfo: String = "Annotation"
    }
    object Field extends SymbolKind {
        override val code: Int = 5
        override val letter: Char = 'f'
        override def getDebugInfo: String = "Field"
    }
    object Constructor extends SymbolKind {
        override val code: Int = 4
        override val letter: Char = 'c'
        override def getDebugInfo: String = "Constructor"
    }
    object Enum extends SymbolKind {
        override val code: Int = 13
        override val letter: Char = 'E'
        override def getDebugInfo: String = "Enum"
    }
    object EnumMember extends SymbolKind {
        override val code: Int = 20
        override val letter: Char = 'e'
        override def getDebugInfo: String = "EnumMember"
    }
    object Function extends SymbolKind {
        override val code: Int = 3
        override val letter: Char = 'm'
        override def getDebugInfo: String = "Function"
    }
    object Interface extends SymbolKind {
        override val code: Int = 8
        override val letter: Char = 'i'
        override def getDebugInfo: String = "Interface"
    }
    object Variable extends SymbolKind {
        override val code: Int = 6
        override val letter: Char = 'v'
        override def getDebugInfo: String = "Variable"
    }

    object Reference extends SymbolKind {
        override val code: Int = 18
        override val letter: Char = 'r'
        override def getDebugInfo: String = "Reference"
    }

    object Namespace extends SymbolKind {
        override val code: Int = 9
        override val letter: Char = 'N'
        override def getDebugInfo: String = "Namespace"
    }
}

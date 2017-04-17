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

package com.neowit.apex.symbols

/**
  * Created by Andrey Gavrikov
  *
  * symbol kinds are modeled after
  * interface SymbolKind from LSP:
  * https://github.com/Microsoft/language-server-protocol/blob/master/protocol.md
  */
trait SymbolKind {
    val code: Int
}
object SymbolKind {
    object File extends SymbolKind {
        override val code: Int = 1
    }
    object Trigger extends SymbolKind {
        // this is not mistake, LSP does not support a ntion of Trigger
        override val code: Int = File.code
    }
    object Class extends SymbolKind {
        override val code: Int = 5
    }
    object Method extends SymbolKind {
        override val code: Int = 6
    }
    object Property extends SymbolKind {
        override val code: Int = 7
    }
    object Field extends SymbolKind {
        override val code: Int = 8
    }
    object Constructor extends SymbolKind {
        override val code: Int = 9
    }
    object Enum extends SymbolKind {
        override val code: Int = 10
    }
    object Interface extends SymbolKind {
        override val code: Int = 11
    }
    object Variable extends SymbolKind {
        override val code: Int = 13
    }

}
